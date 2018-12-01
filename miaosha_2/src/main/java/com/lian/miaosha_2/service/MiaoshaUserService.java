package com.lian.miaosha_2.service;

import com.lian.miaosha_2.domain.MiaoshaUser;
import com.lian.miaosha_2.exception.GlobalException;
import com.lian.miaosha_2.mapper.MiaoshaUserDao;
import com.lian.miaosha_2.redis.MiaoshaUserPrefix;
import com.lian.miaosha_2.redis.RedisService;
import com.lian.miaosha_2.result.CodeMsg;
import com.lian.miaosha_2.util.MD5Util;
import com.lian.miaosha_2.util.UUIDUtil;
import com.lian.miaosha_2.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @Author:L1ANN
 * @Description:
 * @Date:Created in 下午3:27 2018/10/27
 * @Modified By:
 */
@Service
public class MiaoshaUserService {

    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    private MiaoshaUserDao miaoshaUserDao;

    @Autowired
    private RedisService redisService;

    public MiaoshaUser getById(long id) {
        return miaoshaUserDao.getById(id);

    }

    public MiaoshaUser getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token))
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        MiaoshaUser user = redisService.get(MiaoshaUserPrefix.token, token, MiaoshaUser.class);
        if (user != null) {
            addCookie(response, token, user);
        }
        return user;

    }

    public boolean login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        //判断手机号是否存在
        MiaoshaUser user = getById(Long.valueOf(mobile));
        if (user == null) {

            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);

        }
        String pass = user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);
        if (!calcPass.equals(pass)) {

            throw new GlobalException(CodeMsg.PASSWORD_ERROR);

        }

        String token = UUIDUtil.uuid();
        addCookie(response, token, user);

        return true;
    }

    private void addCookie(HttpServletResponse response, String token, MiaoshaUser user) {
        //用户登录成功后，生成token，标识用户，写到cookie，传递到客户端
        //客户端在随后的请求中，在cookie中传递token，服务器接受到token后，到redis中获取
        redisService.set(MiaoshaUserPrefix.token, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(MiaoshaUserPrefix.token.expireSeconds()); //默认保存token的cookie的有效期与缓存的有效期相同
        cookie.setPath("/"); //设置保存cookie的路径，默认是网站的根路径
        response.addCookie(cookie);
    }
}
