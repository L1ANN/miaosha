package com.lian.miaosha_7.redis.prefix;

/**
 * @author: lianpengfei
 * @version: 1.0
 * @create: 2018/11/26
 */
public class MiaoshaPrefix extends BasePrefix{
    private MiaoshaPrefix(String prefix){
        super(prefix);
    }
    private MiaoshaPrefix(int expire,String prefix){
        super(expire,prefix);
    }

    public static final MiaoshaPrefix IS_GOOS_OVER = new MiaoshaPrefix("igo");
    public static final MiaoshaPrefix GET_MIAOSHA_PATH =  new MiaoshaPrefix(60,"gmp");
    public static final MiaoshaPrefix ACCESS_NUMBER = new MiaoshaPrefix(5,"an");
}
