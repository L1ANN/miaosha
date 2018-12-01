package com.lian.miaosha_1.result;

/**
 * @Author:L1ANN
 * @Description:
 * @Date:Created in 下午5:26 2018/10/16
 * @Modified By:
 */

/**
 * 调用失败时，构建Result对象需要传入的code和message
 */
public class CodeMsg {
    private int code;
    private String msg;

    private CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    //通用异常
    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常");

    //登录模块 5002XX

    //商品模块 5003XX

    //订单模块 5004XX

    //秒杀模块 5005XX

    public int getCode() {
        return code;
    }


    public String getMsg() {
        return msg;
    }

}
