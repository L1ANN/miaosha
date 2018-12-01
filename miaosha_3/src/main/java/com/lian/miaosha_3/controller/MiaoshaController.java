package com.lian.miaosha_3.controller;

import com.lian.miaosha_3.domain.MiaoshaOrder;
import com.lian.miaosha_3.domain.MiaoshaUser;
import com.lian.miaosha_3.domain.OrderInfo;
import com.lian.miaosha_3.result.CodeMsg;
import com.lian.miaosha_3.service.GoodsService;
import com.lian.miaosha_3.service.MiaoshaService;
import com.lian.miaosha_3.service.OrderService;
import com.lian.miaosha_3.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author:L1ANN
 * @Description:
 * @Date:Created in 下午10:12 2018/11/15
 * @Modified By:
 */
@Controller
@RequestMapping("/misosha")
public class MiaoshaController {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MiaoshaService miaoshaService;

    @RequestMapping("/do_miaosha")
    public String list(Model model, MiaoshaUser user, @RequestParam("goodsId") long goodsId) {
        model.addAttribute("user", user);

        if (user == null) {
            return "login";
        }

        //判断库存
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goodsVo.getStockCount();
        if (stock <= 0) {
            model.addAttribute("errmsg", CodeMsg.MIAOSHA_OVER.getMsg());
            return "miaosha_fail";
        }

        //判断是否已经秒杀到
        MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (miaoshaOrder != null) {
            model.addAttribute("errmsg", CodeMsg.MIAOSHA_REPEAT.getMsg());
        }

        //减库存，下订单，写入秒杀订单（需要在事务中实现）
        OrderInfo orderInfo = miaoshaService.miaosha(user.getId(), goodsVo);
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goods", goodsVo);
        return "order_detail";
    }
}

