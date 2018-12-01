package com.lian.miaosha_1.controller;

import com.lian.miaosha_1.domain.User;
import com.lian.miaosha_1.redis.RedisService;
import com.lian.miaosha_1.redis.prefix.UserPrefix;
import com.lian.miaosha_1.result.CodeMsg;
import com.lian.miaosha_1.result.Result;
import com.lian.miaosha_1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author:L1ANN
 * @Description:
 * @Date:Created in 下午5:39 2018/10/16
 * @Modified By:
 */
@Controller
@RequestMapping("/demo")
public class DemoController {
    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @RequestMapping("/success")
    @ResponseBody
    public Result<String> success() {
        return Result.success("hello,mooc");
    }

    @RequestMapping("/error")
    @ResponseBody
    public Result<String> error() {
        return Result.error(CodeMsg.SERVER_ERROR);
    }

    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model) {
        model.addAttribute("name", "Thymeleaf");

        return "hello";
    }

    @RequestMapping("/db/{id}")
    @ResponseBody
    public Result<User> db(@PathVariable("id") int id) {
        User user = userService.getById(id);
        return Result.success(user);
    }

    @RequestMapping("/tx")
    @ResponseBody
    public boolean db() {
        return userService.insert();
    }

    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<User> getRedis() {
        User res = redisService.get(UserPrefix.getById, "1", User.class);
        return Result.success(res);
    }

    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> setRedis() {
        User user = new User(1, "lianpengfei");
        Boolean res = redisService.set(UserPrefix.getById, "1", user);
        return Result.success(res);
    }
}
