package com.lian.miaosha_7.controller;

import com.lian.miaosha_7.config.AccessLimit;
import com.lian.miaosha_7.domain.MiaoshaMessage;
import com.lian.miaosha_7.domain.MiaoshaOrder;
import com.lian.miaosha_7.domain.MiaoshaUser;
import com.lian.miaosha_7.rabbitmq.MQSender;
import com.lian.miaosha_7.redis.RedisService;
import com.lian.miaosha_7.redis.prefix.GoodsPrefix;
import com.lian.miaosha_7.redis.prefix.MiaoshaPrefix;
import com.lian.miaosha_7.result.CodeMsg;
import com.lian.miaosha_7.result.Result;
import com.lian.miaosha_7.service.GoodsService;
import com.lian.miaosha_7.service.MiaoshaService;
import com.lian.miaosha_7.service.OrderService;
import com.lian.miaosha_7.util.MD5Util;
import com.lian.miaosha_7.util.UUIDUtil;
import com.lian.miaosha_7.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:L1ANN
 * @Description:
 * @Date:Created in 下午10:12 2018/11/15
 * @Modified By:
 */
@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private MiaoshaService miaoshaService;
    @Autowired
    private MQSender mqSender;

    private Map<Long, Boolean> localOverMap = new HashMap<>(); //使用成员变量，保存商品是否超卖

    private static final String SALT = "123456";

    @RequestMapping("/{path}/do_miaosha")
    @ResponseBody
    public Result list(Model model, MiaoshaUser user, @RequestParam("goodsId") long goodsId, @PathVariable String path) {
        model.addAttribute("user", user);

        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        boolean checkRes = miaoshaService.checkPath(user,goodsId,path);
        if(!checkRes){
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }

        /**
         * 如果库存为空，直接返回
         */
        boolean isOver = localOverMap.get(goodsId);
        if (isOver) {
            return Result.error(CodeMsg.MIAOSHA_OVER);
        }
        /**
         * 预减库存，返回库存-1后的库存数
         */
        long stock = redisService.decr(GoodsPrefix.GET_GOODS_NUMBER, "" + goodsId);
        if (stock < 0) {
            localOverMap.put(goodsId, true);
            return Result.error(CodeMsg.MIAOSHA_OVER);
        }

        /**
         * 判断是否已经秒杀到
         */
        MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (miaoshaOrder != null) {
            return Result.error(CodeMsg.MIAOSHA_REPEAT);
        }

        MiaoshaMessage miaoshaMessage = new MiaoshaMessage();
        miaoshaMessage.setMiaoshaUser(user);
        miaoshaMessage.setGoodsId(goodsId);
        mqSender.sendMiaoshaMessage(miaoshaMessage);
        return Result.success(0); //排队中

//        //判断库存
//        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
//        int stock = goodsVo.getStockCount();
//        if (stock <= 0) {
//            return Result.error(CodeMsg.MIAOSHA_OVER);
//        }

//        //判断是否已经秒杀到
//        MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
//        if (miaoshaOrder != null) {
//            return Result.error(CodeMsg.MIAOSHA_REPEAT);
//        }

//        //减库存，下订单，写入秒杀订单（需要在事务中实现）
//        OrderInfo orderInfo = miaoshaService.miaosha(user.getId(), goodsVo);
//        return Result.success(orderInfo);
    }

    /**
     * 返回秒杀结果
     * 秒杀成功：返回orderId
     * 秒杀失败：返回-1
     * 排队中：返回0
     *
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public Result result(Model model, MiaoshaUser user, @RequestParam("goodsId") long goodsId) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        long res = miaoshaService.getMiaoshaResult(goodsId, user);
        return Result.success(res);
    }

    @AccessLimit(seconds=5,maxCount=5,needLogin=true)
    @RequestMapping(value = "/getMiaoshaPath", method = RequestMethod.GET)
    @ResponseBody
    public Result getMiaoshaPath(HttpServletRequest request, MiaoshaUser user, @RequestParam("goodsId") long goodsId) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        String uri = request.getRequestURI();
        Integer accNumber = redisService.get(MiaoshaPrefix.ACCESS_NUMBER,user.getId()+"_"+uri,Integer.class);
        if (accNumber == null){
            redisService.set(MiaoshaPrefix.ACCESS_NUMBER,user.getId()+"_"+uri,1);
        }else if(accNumber <= 5){
            redisService.incr(MiaoshaPrefix.ACCESS_NUMBER,user.getId()+"_"+uri);
        }else{
            return Result.error(CodeMsg.ACCESS_LIMIT);
        }
        String generatePath = miaoshaService.getPath(user,goodsId);
        return Result.success(generatePath);
    }

    /**
     * 系统初始化时，将所有秒杀商品的库存加到缓存中
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
        if (goodsVoList == null) {
            return;
        }
        goodsVoList.forEach(goodsVo -> {
            redisService.set(GoodsPrefix.GET_GOODS_NUMBER, "" + goodsVo.getGoodsId(), goodsVo.getStockCount());
            localOverMap.put(goodsVo.getGoodsId(), false);
        });
    }
}

