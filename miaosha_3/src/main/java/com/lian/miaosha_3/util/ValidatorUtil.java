package com.lian.miaosha_3.util;


import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author:L1ANN
 * @Description: 参数验证工具类
 * @Date:Created in 下午2:58 2018/10/27
 * @Modified By:
 */
public class ValidatorUtil {
    private static final Pattern mobile_pattern = Pattern.compile("^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$");

    public static boolean isMobile(String src) {
        if (StringUtils.isEmpty(src)) {
            return false;
        }
        Matcher m = mobile_pattern.matcher(src);
        return m.matches();
    }

    public static void main(String[] args) {
        System.out.println(isMobile("13588254029"));
    }

}
