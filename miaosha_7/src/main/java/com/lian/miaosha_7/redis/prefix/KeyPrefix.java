package com.lian.miaosha_7.redis.prefix;

/**
 * @Author:L1ANN
 * @Description:
 * @Date:Created in 下午7:04 2018/10/23
 * @Modified By:
 */
public interface KeyPrefix {
    public int expireSeconds();

    public String getPrefix();
}
