package com.lian.miaosha_3.mapper;

import com.lian.miaosha_3.domain.MiaoshaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @Author:L1ANN
 * @Description:
 * @Date:Created in 下午3:35 3018/10/37
 * @Modified By:
 */
@Mapper
public interface MiaoshaUserDao {
    @Select("select * from miaosha_user where id=#{id}")
    public MiaoshaUser getById(@Param("id") long id);
}
