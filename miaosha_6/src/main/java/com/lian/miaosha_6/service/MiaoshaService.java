package com.lian.miaosha_6.service;

import com.lian.miaosha_6.domain.MiaoshaOrder;
import com.lian.miaosha_6.domain.MiaoshaUser;
import com.lian.miaosha_6.domain.OrderInfo;
import com.lian.miaosha_6.redis.RedisService;
import com.lian.miaosha_6.redis.prefix.MiaoshaPrefix;
import com.lian.miaosha_6.vo.GoodsVo;
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
    @Autowired
    private RedisService redisService;

    /**
     * 减库存，下订单，写入秒杀订单
     *
     * @param userId
     * @param goodsVo
     * @return
     */
    @Transactional
    public OrderInfo miaosha(Long userId, GoodsVo goodsVo) {

        boolean success = goodsService.reduceStock(goodsVo);

        if(success){
            return orderService.createOrder(userId, goodsVo);
        }else{
            /**
             * 标记商品已经秒杀完
             */
            setGoodsOver(goodsVo.getGoodsId());
            return null;
        }

    }

    public long getMiaoshaResult(long goodsId, MiaoshaUser user) {
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goodsId);
        if(order != null){
            return order.getOrderId();
        }else{
            /**
             * 判断商品是否秒杀完，如果秒杀完，且订单为null，则秒杀失败；如果没有秒杀完，且订单为null，则排队中
             */
            boolean isOver = getGoodsOver(goodsId);
            if(isOver){
                return -1;
            }else{
                return 0;
            }
        }
    }

    private void setGoodsOver(Long goodsId) {
        redisService.set(MiaoshaPrefix.IS_GOOS_OVER,""+goodsId,true);
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(MiaoshaPrefix.IS_GOOS_OVER,""+goodsId);

    }
}
