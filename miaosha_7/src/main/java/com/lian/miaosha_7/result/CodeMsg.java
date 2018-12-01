package com.lian.miaosha_7.result;

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
    public static CodeMsg BIND_ERROR = new CodeMsg(500101, "参数绑定异常:%s"); //需要绑定参数
    public static CodeMsg REQUEST_ILLEGAL = new CodeMsg(500102,"请求非法");
    public static CodeMsg ACCESS_LIMIT = new CodeMsg(500103,"访问太频繁");

    //登录模块 5002XX
    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500211, "登录密码不能为空");
    public static CodeMsg MOBILE_EMPTY = new CodeMsg(500212, "手机号码不能为空");
    public static CodeMsg MOBILE_ERROR = new CodeMsg(500213, "手机号码格式错误");
    public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500214, "手机号码不存在");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500215, "密码错误");
    public static CodeMsg SESSION_ERROR = new CodeMsg(500216,"session过期");

    //商品模块 5003XX
    //订单模块 5004XX
    public static CodeMsg ORDER_NOT_EXIST = new CodeMsg(500411,"订单不存在");

    //秒杀模块 5005XX
    public static CodeMsg MIAOSHA_OVER = new CodeMsg(500500, "商品已经秒杀完毕");
    public static CodeMsg MIAOSHA_REPEAT = new CodeMsg(500501, "不能重复秒杀");


    public int getCode() {
        return code;
    }


    public String getMsg() {
        return msg;
    }

    /**
     * 绑定参数
     */
    public CodeMsg fillArgs(Object... args) {
        int code = this.code;
        String message = String.format(this.msg, args);
        return new CodeMsg(code, message);
    }

}
