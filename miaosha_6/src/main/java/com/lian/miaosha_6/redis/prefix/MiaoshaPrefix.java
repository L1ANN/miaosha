package com.lian.miaosha_6.redis.prefix;

/**
 * @author: lianpengfei
 * @version: 1.0
 * @create: 2018/11/26
 */
public class MiaoshaPrefix extends BasePrefix{
    private MiaoshaPrefix(String prefix){
        super(prefix);
    }

    public static final MiaoshaPrefix IS_GOOS_OVER = new MiaoshaPrefix("igo");
}
