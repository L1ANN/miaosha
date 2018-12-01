package com.lian.miaosha_3.controller;

import com.lian.miaosha_3.domain.MiaoshaGoods;
import com.lian.miaosha_3.domain.MiaoshaOrder;
import com.lian.miaosha_3.domain.MiaoshaUser;
import com.lian.miaosha_3.domain.OrderInfo;
import com.lian.miaosha_3.result.CodeMsg;
import com.lian.miaosha_3.service.GoodsService;
import com.lian.miaosha_3.service.MiaoshaService;
import com.lian.miaosha_3.service.MiaoshaUserService;
import com.lian.miaosha_3.service.OrderService;
import com.lian.miaosha_3.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author:L1ANN
 * @Description:
 * @Date:Created in 上午10:14 2018/10/29
 * @Modified By:
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private MiaoshaUserService miaoshaUserService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MiaoshaService miaoshaService;


    @RequestMapping("/to_list")
    public String list(HttpServletResponse response, Model model, MiaoshaUser miaoshaUser) {

        model.addAttribute("user", miaoshaUser);

        //查询商品列表
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);
        return "goods_list";
    }

    @RequestMapping("/to_detail/{goodsId}")
    public String detail(Model model, MiaoshaUser user, @PathVariable("goodsId") long goodsId) {
        model.addAttribute("user", user);

        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goodsVo);

        //秒杀开始时间
        long startAt = goodsVo.getStartDate().getTime();
        long endAt = goodsVo.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int miaoshaStatus = 0;//秒杀状态
        int remainSeconds = 0;//秒杀开始剩余时间

        if (now < startAt) { //秒杀还没开始
            miaoshaStatus = 0;
            remainSeconds = (int) (startAt - now) / 1000;
        } else if (now > endAt) { //秒杀结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        } else {//秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        return "goods_detail";
    }

}
