package com.lian.miaosha_5.service;

import com.lian.miaosha_5.domain.MiaoshaOrder;
import com.lian.miaosha_5.domain.OrderInfo;
import com.lian.miaosha_5.mapper.OrderDao;
import com.lian.miaosha_5.redis.RedisService;
import com.lian.miaosha_5.redis.prefix.OrderPrefix;
import com.lian.miaosha_5.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Author:L1ANN
 * @Description:
 * @Date:Created in 下午6:31 2018/11/15
 * @Modified By:
 */
@Service
public class OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private RedisService redisService;


    public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(Long userId, Long goodsId) {
        return redisService.get(OrderPrefix.getMiaoshaOrderByUidGid, "" + userId + "_" + goodsId, MiaoshaOrder.class);
    }

    @Transactional
    public OrderInfo createOrder(Long userId, GoodsVo goodsVo) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setUserId(userId);
        orderInfo.setGoodsId(goodsVo.getGoodsId());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsName(goodsVo.getGoodsName());
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsPrice(goodsVo.getMiaoshaPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setCreateDate(new Date());
        orderInfo.setPayDate(new Date());
        orderDao.insert(orderInfo);

        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setUserId(userId);
        miaoshaOrder.setGoodsId(goodsVo.getId());
        miaoshaOrder.setOrderId(orderInfo.getId());
        orderDao.insertMiaoshaOrder(miaoshaOrder);

        redisService.set(OrderPrefix.getMiaoshaOrderByUidGid, "" + userId + "_" + goodsVo.getId(), miaoshaOrder);

        return orderInfo;
    }

    public OrderInfo getOrderByOrderId(long orderId) {
        return orderDao.getOrderById(orderId);
    }
}
