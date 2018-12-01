package com.lian.miaosha_3.service;

import com.lian.miaosha_3.domain.User;
import com.lian.miaosha_3.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author:L1ANN
 * @Description:
 * @Date:Created in 下午11:02 2018/10/17
 * @Modified By:
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User getById(int id) {
        return userMapper.selectById(id);
    }

    //    @Transactional
    public boolean insert() {
        User user1 = new User(5, "5");

        userMapper.insertUser(user1);

        int i = 1 / 0;

        return true;
    }
}
