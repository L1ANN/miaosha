package com.lian.miaosha_2.util;

import java.util.UUID;

/**
 * @Author:L1ANN
 * @Description:
 * @Date:Created in 上午9:48 2018/10/29
 * @Modified By:
 */
public class UUIDUtil {
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
