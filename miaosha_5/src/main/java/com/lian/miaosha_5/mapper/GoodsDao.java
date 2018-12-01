package com.lian.miaosha_5.mapper;

import com.lian.miaosha_5.domain.MiaoshaGoods;
import com.lian.miaosha_5.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @Author:L1ANN
 * @Description:
 * @Date:Created in 下午9:14 2018/11/13
 * @Modified By:
 */
@Mapper
public interface GoodsDao {
    @Select("select mg.*,g.* from miaosha_goods mg left join goods g on mg.goods_id = g.id")
    List<GoodsVo> listGoodsVo();

    @Select("select mg.*,g.* from miaosha_goods mg left join goods g on mg.goods_id = g.id where g.id= #{goodsId}")
    GoodsVo getGoodsVoByGoodsId(@Param("goodsId") long goodsId);

    @Update("update miaosha_goods set stock_count = #{stockCount} where goods_id = #{id} and stock_count > 0")
    int reduceStock(MiaoshaGoods miaoshaGoods);
}
