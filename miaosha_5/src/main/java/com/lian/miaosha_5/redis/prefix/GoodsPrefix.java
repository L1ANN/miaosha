package com.lian.miaosha_5.redis.prefix;

import com.fasterxml.jackson.databind.ser.Serializers;

/**
 * @Author:L1ANN
 * @Description:
 * @Date:Created in 下午6:50 2018/11/19
 * @Modified By:
 */
public class GoodsPrefix extends BasePrefix {
    private GoodsPrefix(int expireSeconds,String prefix){
        super(expireSeconds,prefix);
    }

    public static final GoodsPrefix GET_GOODS_LIST = new GoodsPrefix(60,"goodslist");
    public static final GoodsPrefix GET_GOODS_DETAIL = new GoodsPrefix(60,"gooddetail");

}
