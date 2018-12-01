package com.lian.miaosha_6.controller;

import com.lian.miaosha_6.domain.MiaoshaUser;
import com.lian.miaosha_6.result.Result;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author:L1ANN
 * @Description:
 * @Date:Created in 下午1:17 2018/11/17
 * @Modified By:
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @ResponseBody
    @RequestMapping("/info")
    public Result<MiaoshaUser> info(Model model, MiaoshaUser user) {
        return Result.success(user);
    }
}
