package com.lian.miaosha_7.rabbitmq;

import com.lian.miaosha_7.domain.MiaoshaMessage;
import com.lian.miaosha_7.domain.MiaoshaOrder;
import com.lian.miaosha_7.domain.MiaoshaUser;
import com.lian.miaosha_7.redis.RedisService;
import com.lian.miaosha_7.service.GoodsService;
import com.lian.miaosha_7.service.MiaoshaService;
import com.lian.miaosha_7.service.OrderService;
import com.lian.miaosha_7.vo.GoodsVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: lianpengfei
 * @version: 1.0
 * @create: 2018/11/25
 */
@Service
public class MQReceiver {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private MiaoshaService miaoshaService;

    private static final Logger LOGGER = LogManager.getLogger(MQSender.class);

    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
    public void receive(String message) {
        LOGGER.info("从秒杀队列接受消息:" + message);
        MiaoshaMessage miaoshaMessage = RedisService.stringToBean(message,MiaoshaMessage.class);
        MiaoshaUser user = miaoshaMessage.getMiaoshaUser();
        long goodsId = miaoshaMessage.getGoodsId();

        //判断库存
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goodsVo.getStockCount();
        if (stock <= 0) {
            return;
        }

        //判断是否已经秒杀到
        MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (miaoshaOrder != null) {
            return;
        }

        //减库存，下订单，写入秒杀订单（需要在事务中实现）
        miaoshaService.miaosha(user.getId(), goodsVo);
    }

//    @RabbitListener(queues = MQConfig.QUEUE)
//    public void receive(String message) {
//        LOGGER.info("接受消息:" + message);
//    }
//
//    /**
//     * Receive1与topic_queue1队列绑定
//     *
//     * @param message
//     */
//    @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
//    public void topicReceive1(String message) {
//        LOGGER.info("topicQueue1接受消息:" + message);
//    }
//
//    /**
//     * Receive2与topic_queue2队列绑定
//     *
//     * @param message
//     */
//    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
//    public void topicReceive2(String message) {
//        LOGGER.info("topicQueue2接受消息:" + message);
//    }
//
//    /**
//     * 注意这里要接受字节数组的参数，因为发送时发送的时字节数组
//     * @param message
//     */
//    @RabbitListener(queues = MQConfig.HEADER_QUEUE)
//    public void headerReceive(byte[] message) {
//        LOGGER.info("headerQueue接受消息:" + new String(message));
//    }


}
