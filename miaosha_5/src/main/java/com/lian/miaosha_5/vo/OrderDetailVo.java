package com.lian.miaosha_5.vo;

import com.lian.miaosha_5.domain.OrderInfo;

/**
 * @author: lianpengfei
 * @version: 1.0
 * @create: 2018/11/21
 */
public class OrderDetailVo {
    private GoodsVo goods;
    private OrderInfo order;

    public GoodsVo getGoods() {
        return goods;
    }

    public void setGoods(GoodsVo goods) {
        this.goods = goods;
    }

    public OrderInfo getOrder() {
        return order;
    }

    public void setOrder(OrderInfo order) {
        this.order = order;
    }
}
