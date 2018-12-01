package com.lian.miaosha_3.service;

import com.lian.miaosha_3.domain.OrderInfo;
import com.lian.miaosha_3.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author:L1ANN
 * @Description:
 * @Date:Created in 下午6:53 2018/11/15
 * @Modified By:
 */
@Service
public class MiaoshaService {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;

    /**
     * 减库存，下订单，写入秒杀订单
     *
     * @param userId
     * @param goodsVo
     * @return
     */
    @Transactional
    public OrderInfo miaosha(Long userId, GoodsVo goodsVo) {

        goodsService.reduceStock(goodsVo);

        return orderService.createOrder(userId, goodsVo);
    }
}
