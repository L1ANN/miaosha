package com.lian.miaosha_7.controller;

import com.lian.miaosha_7.domain.MiaoshaUser;
import com.lian.miaosha_7.redis.RedisService;
import com.lian.miaosha_7.redis.prefix.GoodsPrefix;
import com.lian.miaosha_7.result.Result;
import com.lian.miaosha_7.service.GoodsService;
import com.lian.miaosha_7.service.MiaoshaService;
import com.lian.miaosha_7.service.MiaoshaUserService;
import com.lian.miaosha_7.service.OrderService;
import com.lian.miaosha_7.vo.GoodsDetailVo;
import com.lian.miaosha_7.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.*;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

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
    @Autowired
    private RedisService redisService;
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;
    @Autowired
    private ApplicationContext applicationContext;

    @RequestMapping(value="/to_list",produces = "text/html")
    @ResponseBody
    public String list(HttpServletRequest request, HttpServletResponse response, Model model, MiaoshaUser miaoshaUser) {

        model.addAttribute("user", miaoshaUser);

        //如果缓存中有，则从缓存中取
        String html = redisService.get(GoodsPrefix.GET_GOODS_LIST, "", String.class);
        if(isNotBlank(html)){
            return html;
        }
        //如果没有，手动渲染页面，保存到缓存中，返回
        //查询商品列表
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);

        WebContext ctx = new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list",ctx);
        if(isNotBlank(html)){
            redisService.set(GoodsPrefix.GET_GOODS_LIST,"",html);
        }
        return html;
    }


    @RequestMapping(value = "/detail/{goodsId}")
    @ResponseBody
    public Result detail(MiaoshaUser user, @PathVariable("goodsId") long goodsId) {
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
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
        GoodsDetailVo goodsDetailVo = new GoodsDetailVo();
        goodsDetailVo.setGoods(goodsVo);
        goodsDetailVo.setUser(user);
        goodsDetailVo.setMiaoshaStatus(miaoshaStatus);
        goodsDetailVo.setRemainSeconds(remainSeconds);

        return Result.success(goodsDetailVo);

    }


    @RequestMapping(value = "/to_detail2/{goodsId}",produces = "text/html")
    @ResponseBody
    public String detail2(HttpServletRequest request, HttpServletResponse response,Model model, MiaoshaUser user, @PathVariable("goodsId") long goodsId) {
        model.addAttribute("user", user);

        String html = redisService.get(GoodsPrefix.GET_GOODS_DETAIL,""+goodsId,String.class);
        if(isNotBlank(html)){
            return html;
        }
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

        WebContext ctx = new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail",ctx);
        if(isNotBlank(html)){
            redisService.set(GoodsPrefix.GET_GOODS_DETAIL,""+goodsId,html);
        }
        return html;
    }


}
