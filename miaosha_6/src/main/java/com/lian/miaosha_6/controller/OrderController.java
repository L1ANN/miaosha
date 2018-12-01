package com.lian.miaosha_6.controller;/**
 * @author: lianpengfei
 * @version: 1.0
 * @create: 2018/11/21
 */

import com.lian.miaosha_6.domain.MiaoshaUser;
import com.lian.miaosha_6.domain.OrderInfo;
import com.lian.miaosha_6.result.CodeMsg;
import com.lian.miaosha_6.result.Result;
import com.lian.miaosha_6.service.GoodsService;
import com.lian.miaosha_6.service.OrderService;
import com.lian.miaosha_6.vo.GoodsVo;
import com.lian.miaosha_6.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 *
 * @author: lianpengfei
 * @version: 1.0
 * @create: 2018/11/21
 */
@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private GoodsService goodsService;

    @RequestMapping("/detail")
    @ResponseBody
    public Result info(MiaoshaUser user, @RequestParam("orderId")long orderId){
        if(user == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        OrderInfo order = orderService.getOrderByOrderId(orderId);
        if(order == null) {
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }

        GoodsVo good = goodsService.getGoodsVoByGoodsId(order.getGoodsId());

        OrderDetailVo orderDetail = new OrderDetailVo();
        orderDetail.setOrder(order);
        orderDetail.setGoods(good);

        return Result.success(orderDetail);

    }

}
