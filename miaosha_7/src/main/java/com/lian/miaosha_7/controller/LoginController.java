package com.lian.miaosha_7.controller;

import com.lian.miaosha_7.result.Result;
import com.lian.miaosha_7.service.MiaoshaUserService;
import com.lian.miaosha_7.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Collections;


/**
 * @Author:L1ANN
 * @Description:
 * @Date:Created in 下午12:59 2018/10/27
 * @Modified By:
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    private static Logger log = LogManager.getLogger(LoginController.class);

    @Autowired
    private MiaoshaUserService miaoshaUserService;

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<Boolean> doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {
        log.info(loginVo.toString());

        miaoshaUserService.login(response, loginVo);
        return Result.success(true);
    }
}
