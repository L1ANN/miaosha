package com.lian.miaosha_7.exception;

import com.lian.miaosha_7.controller.LoginController;
import com.lian.miaosha_7.result.CodeMsg;
import com.lian.miaosha_7.result.Result;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author:L1ANN
 * @Description:
 * @Date:Created in 下午4:40 2018/10/28
 * @Modified By:
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    /**
     * ExceptionHandler：定义拦截的异常
     * 拦截业务异常和参数校验异常，如果均不是，抛出服务器异常
     *
     * @return
     */
    private static Logger LOGGER = LogManager.getLogger(LoginController.class);

    @ExceptionHandler(value = Exception.class)
    public Result<String> exceptionHandler(HttpServletRequest request, Exception e) {
        LOGGER.error("异常错误", e);
        if (e instanceof GlobalException) {
            GlobalException ex = (GlobalException) e;
            return Result.error(ex.getCm());
        } else if (e instanceof BindException) {
            BindException ex = (BindException) e;
            //获取所有参数校验的错误
            List<ObjectError> errors = ex.getAllErrors();
            ObjectError error = errors.get(0);
            String msg = error.getDefaultMessage();
            return Result.error(CodeMsg.BIND_ERROR.fillArgs(msg));
        } else {

            return Result.error(CodeMsg.SERVER_ERROR);
        }

    }
}
