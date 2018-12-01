package com.lian.miaosha_5.redis.prefix;

/**
 * @author: lianpengfei
 * @version: 1.0
 * @create: 2018/11/26
 */
public class OrderPrefix extends BasePrefix{
    private OrderPrefix(String prefix){
        super(prefix);
    }

    public static final OrderPrefix getMiaoshaOrderByUidGid = new OrderPrefix("moug");
}
