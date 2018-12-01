package com.lian.miaosha_6.result;

/**
 * @Author:L1ANN
 * @Description:
 * @Date:Created in 下午5:05 2018/10/16
 * @Modified By:
 */
public class Result<T> {
    private int code;
    private String msg;
    private T data;

    private Result(T data) {
        this.code = 0;
        this.msg = "success";
        this.data = data;
    }

    private Result(CodeMsg cm) {
        if (cm == null) {
            return;
        }
        this.code = cm.getCode();
        this.msg = cm.getMsg();
    }

    /**
     * 调用成功时，返回的结果
     * 只需要传data，code和msg固定
     */
    public static <T> Result<T> success(T data) {
        return new Result(data);
    }

    /**
     * 调用失败时，返回的结果
     * 传CodeMsg，无data
     */
    public static <T> Result<T> error(CodeMsg cm) {
        return new Result(cm);
    }

    public int getCode() {
        return code;
    }


    public String getMsg() {
        return msg;
    }


    public T getData() {
        return data;
    }

}
