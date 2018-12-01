package com.lian.miaosha_6.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @Author:L1ANN
 * @Description:
 * @Date:Created in 下午12:13 2018/10/27
 * @Modified By:
 */
public class MD5Util {
    private static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    private static final String salt = "1a2b3c4d";

    public static String inputPassToFormPass(String inputPass) {
        String str = "" + salt.charAt(0) + salt.charAt(2) + inputPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    public static String formPassToDBPass(String formPass, String salt) {
        String str = "" + salt.charAt(0) + salt.charAt(2) + formPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);

    }

    public static String inputPassToDBPass(String inputPass, String saltDB) {
        String formPass = inputPassToFormPass(inputPass);
        String DBPass = formPassToDBPass(formPass, saltDB);
        return DBPass;
    }

    public static void main(String[] args) {
        System.out.println(inputPassToDBPass("123456", "654321"));
    }
}
