package com.lian.miaosha_6.service;

import com.lian.miaosha_6.domain.MiaoshaUser;
import com.lian.miaosha_6.exception.GlobalException;
import com.lian.miaosha_6.mapper.MiaoshaUserDao;
import com.lian.miaosha_6.redis.MiaoshaUserPrefix;
import com.lian.miaosha_6.redis.RedisService;
import com.lian.miaosha_6.result.CodeMsg;
import com.lian.miaosha_6.util.MD5Util;
import com.lian.miaosha_6.util.UUIDUtil;
import com.lian.miaosha_6.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

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

        //取缓存（注意：当MiaoshaUser发生变化时，需要清理缓存。例如：用户修改个人资料，修改密码）
        MiaoshaUser miaoshaUser = redisService.get(MiaoshaUserPrefix.GET_BYID,""+id,MiaoshaUser.class);
        if(miaoshaUser != null){
            return miaoshaUser;
        }
        //取数据库,保存到缓存中
        miaoshaUser = miaoshaUserDao.getById(id);
        if(miaoshaUser != null){
            redisService.set(MiaoshaUserPrefix.GET_BYID,""+id,MiaoshaUser.class);
        }
        return miaoshaUser;

    }

    public boolean updatePassword(String token,long id,String formPass){
        MiaoshaUser user = miaoshaUserDao.getById(id);
        if(user == null)
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        //更新用户密码
        MiaoshaUser toBeNewUser = new MiaoshaUser();
        toBeNewUser.setId(id);
        toBeNewUser.setPassword(MD5Util.formPassToDBPass(formPass,user.getSalt()));
        miaoshaUserDao.update(toBeNewUser);

        //删除/更新与用户相关的缓存
        redisService.delete(MiaoshaUserPrefix.GET_BYID,""+id);
        user.setPassword(toBeNewUser.getPassword());
        redisService.set(MiaoshaUserPrefix.token,token,user);

        return true;

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
        MiaoshaUser user = miaoshaUserDao.getById(Long.valueOf(mobile));
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
