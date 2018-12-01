package com.lian.miaosha_7.exception;

import com.lian.miaosha_7.result.CodeMsg;

/**
 * @Author:L1ANN
 * @Description: 业务异常类
 * @Date:Created in 下午4:59 2018/10/28
 * @Modified By:
 */
public class GlobalException extends RuntimeException {
    private CodeMsg cm;

    public GlobalException(CodeMsg cm) {
        super(cm.toString());
        this.cm = cm;

    }

    public CodeMsg getCm() {
        return cm;
    }
}
