package com.lian.miaosha_2.redis.prefix;

/**
 * @Author:L1ANN
 * @Description: 用户模块key前缀
 * @Date:Created in 下午7:18 2018/10/23
 * @Modified By:
 */
public class UserPrefix extends BasePrefix {

    private UserPrefix(String prefix) {
        super(prefix);
    }

    public static UserPrefix getById = new UserPrefix("id");
    public static UserPrefix getByName = new UserPrefix("name");

}
