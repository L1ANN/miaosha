package com.lian.miaosha_2.mapper;

import com.lian.miaosha_2.domain.MiaoshaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @Author:L1ANN
 * @Description:
 * @Date:Created in 下午3:25 2018/10/27
 * @Modified By:
 */
@Mapper
public interface MiaoshaUserDao {
    @Select("select * from miaosha_user where id=#{id}")
    public MiaoshaUser getById(@Param("id") long id);
}
