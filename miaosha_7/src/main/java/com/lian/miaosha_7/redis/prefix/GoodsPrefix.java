package com.lian.miaosha_7.redis.prefix;

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
    public static final GoodsPrefix GET_GOODS_NUMBER = new GoodsPrefix(0,"goodsnumber");

}
