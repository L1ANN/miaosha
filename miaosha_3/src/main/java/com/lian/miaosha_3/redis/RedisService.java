package com.lian.miaosha_3.redis;

import com.alibaba.fastjson.JSON;
import com.lian.miaosha_3.redis.prefix.KeyPrefix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @Author:L1ANN
 * @Description: 通过JedisPool获取jedis对象，然后通过jedis获取redis的数据/通过jedis向redis中存数据
 * @Date:Created in 上午9:59 2018/10/22
 * @Modified By:
 */
@Service
public class RedisService {

    @Autowired
    private JedisPool jedisPool;

    /**
     * get方法，传入key，以及要转化的类型clazz，返回T对象,释放jedis
     */
    public <T> T get(KeyPrefix prefix, String key, Class<T> clazz) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey = prefix.getPrefix() + key;
            String str = jedis.get(realKey);
            T t = stringToBean(str, clazz);
            return t;
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 把String转化成clazz对象，特殊类型特殊处理
     * int类型，直接写入
     * long类型，直接写入
     * String类型，直接返回
     */
    private <T> T stringToBean(String str, Class<T> clazz) {
        if (str == null || str.length() <= 0 || clazz == null)
            return null;
        if (clazz == int.class || clazz == Integer.class) {
            return (T) Integer.valueOf(str);
        } else if (clazz == long.class) {
            return (T) Long.valueOf(str);
        } else if (clazz == String.class) {
            return (T) str;
        } else {
            return JSON.toJavaObject(JSON.parseObject(str), clazz);
        }
    }

    /**
     * set方法，传入key和t，返回true,释放jedis
     */
    public <T> boolean set(KeyPrefix prefix, String key, T t) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String str = beanToString(t);
            //生成真正的key
            String realKey = prefix.getPrefix() + key;
            int exp = prefix.expireSeconds();
            //根据前缀设置过期时间
            if (exp <= 0) {
                jedis.set(realKey, str);
            } else {
                jedis.setex(realKey, exp, str);
            }
            return true;
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 把bean转化成String，特殊类型特殊处理
     * int类型，直接写入
     * long类型，直接写入
     * String类型，直接写入
     */
    private <T> String beanToString(T value) {
        if (value == null)
            return null;
        Class<?> clazz = value.getClass();
        if (clazz == int.class || clazz == Integer.class) {
            return "" + value;
        } else if (clazz == long.class) {
            return "" + value;
        } else if (clazz == String.class) {
            return (String) value;
        } else {
            return JSON.toJSONString(value);
        }
    }

    /**
     * 判断指定key是否存在
     */
    public boolean exists(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix + key;
            return jedis.exists(realKey);
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 键值自增1
     */
    public Long incr(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix + key;
            return jedis.incr(realKey);
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 键值自减1
     */
    public Long decr(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix + key;
            return jedis.decr(realKey);
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 关闭jedis连接
     */
    private void returnToPool(Jedis jedis) {
        if (jedis != null)
            jedis.close();
    }
}
