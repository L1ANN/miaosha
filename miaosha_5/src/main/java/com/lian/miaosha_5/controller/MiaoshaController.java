package com.lian.miaosha_5.controller;

import com.lian.miaosha_5.domain.MiaoshaOrder;
import com.lian.miaosha_5.domain.MiaoshaUser;
import com.lian.miaosha_5.domain.OrderInfo;
import com.lian.miaosha_5.result.CodeMsg;
import com.lian.miaosha_5.result.Result;
import com.lian.miaosha_5.service.GoodsService;
import com.lian.miaosha_5.service.MiaoshaService;
import com.lian.miaosha_5.service.OrderService;
import com.lian.miaosha_5.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author:L1ANN
 * @Description:
 * @Date:Created in 下午10:12 2018/11/15
 * @Modified By:
 */
@Controller
@RequestMapping("/miaosha")
public class MiaoshaController {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MiaoshaService miaoshaService;

    @RequestMapping("/do_miaosha")
    @ResponseBody
    public Result list(Model model, MiaoshaUser user, @RequestParam("goodsId") long goodsId) {
        model.addAttribute("user", user);

        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        //判断库存
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goodsVo.getStockCount();
        if (stock <= 0) {
            return Result.error(CodeMsg.MIAOSHA_OVER);
        }

        //判断是否已经秒杀到
        MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (miaoshaOrder != null) {
            return Result.error(CodeMsg.MIAOSHA_REPEAT);
        }

        //减库存，下订单，写入秒杀订单（需要在事务中实现）
        OrderInfo orderInfo = miaoshaService.miaosha(user.getId(), goodsVo);
        return Result.success(orderInfo);
    }
}

