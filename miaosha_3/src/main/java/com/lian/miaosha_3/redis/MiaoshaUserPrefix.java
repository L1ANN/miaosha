package com.lian.miaosha_3.redis;

import com.lian.miaosha_3.redis.prefix.BasePrefix;

/**
 * @Author:L1ANN
 * @Description:
 * @Date:Created in 上午9:58 2018/10/29
 * @Modified By:
 */
public class MiaoshaUserPrefix extends BasePrefix {
    public MiaoshaUserPrefix(int expire, String prefix) {
        super(expire, prefix);
    }

    public static MiaoshaUserPrefix token = new MiaoshaUserPrefix(3600 * 34 * 3, "tk");
}
