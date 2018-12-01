package com.lian.miaosha_6.controller;

import com.lian.miaosha_6.domain.MiaoshaMessage;
import com.lian.miaosha_6.domain.MiaoshaOrder;
import com.lian.miaosha_6.domain.MiaoshaUser;
import com.lian.miaosha_6.domain.OrderInfo;
import com.lian.miaosha_6.rabbitmq.MQSender;
import com.lian.miaosha_6.redis.RedisService;
import com.lian.miaosha_6.redis.prefix.GoodsPrefix;
import com.lian.miaosha_6.result.CodeMsg;
import com.lian.miaosha_6.result.Result;
import com.lian.miaosha_6.service.GoodsService;
import com.lian.miaosha_6.service.MiaoshaService;
import com.lian.miaosha_6.service.OrderService;
import com.lian.miaosha_6.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @RequestMapping("/do_miaosha")
    @ResponseBody
    public Result list(Model model, MiaoshaUser user, @RequestParam("goodsId") long goodsId) {
        model.addAttribute("user", user);

        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        /**
         * 如果库存为空，直接返回
         */
        boolean isOver = localOverMap.get(goodsId);
        if(isOver){
            return Result.error(CodeMsg.MIAOSHA_OVER);
        }
        /**
         * 预减库存，返回库存-1后的库存数
         */
        long stock = redisService.decr(GoodsPrefix.GET_GOODS_NUMBER, "" + goodsId);
        if (stock < 0) {
            localOverMap.put(goodsId,true);
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
            localOverMap.put(goodsVo.getGoodsId(),false);
        });
    }
}

