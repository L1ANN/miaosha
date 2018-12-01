package com.lian.miaosha_2.controller;

import com.lian.miaosha_2.domain.MiaoshaUser;
import com.lian.miaosha_2.domain.User;
import com.lian.miaosha_2.service.MiaoshaUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

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

    @RequestMapping("/to_list")
    public String list(HttpServletResponse response, Model model, MiaoshaUser miaoshaUser) {

        model.addAttribute("user", miaoshaUser);

        return "goods_list";
    }
}
