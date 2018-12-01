package com.lian.miaosha_1.mapper;

import com.lian.miaosha_1.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @Author:L1ANN
 * @Description:
 * @Date:Created in 下午10:59 2018/10/17
 * @Modified By:
 */
@Mapper
public interface UserMapper {

    @Select("select * from user where id= #{id}")
    public User selectById(@Param("id") int id);

    @Insert("insert into user(id,name) values(#{id},#{name})")
    public int insertUser(User user);
}
