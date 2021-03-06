package com.lian.miaosha_5.service;

import com.lian.miaosha_5.domain.MiaoshaGoods;
import com.lian.miaosha_5.mapper.GoodsDao;
import com.lian.miaosha_5.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author:L1ANN
 * @Description:
 * @Date:Created in 下午9:13 2018/11/13
 * @Modified By:
 */
@Service
public class GoodsService {
    @Autowired
    private GoodsDao goodsDao;

    public List<GoodsVo> listGoodsVo() {
        return goodsDao.listGoodsVo();
    }

    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }

    public boolean reduceStock(GoodsVo goodsVo) {
        MiaoshaGoods miaoshaGoods = new MiaoshaGoods();
        miaoshaGoods.setId(goodsVo.getId());
        miaoshaGoods.setStockCount(goodsVo.getStockCount() - 1);
        int res = goodsDao.reduceStock(miaoshaGoods);
        return res > 0;
    }
}
