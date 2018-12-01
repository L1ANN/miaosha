package com.lian.miaosha_6.rabbitmq;

import com.lian.miaosha_6.domain.MiaoshaMessage;
import com.lian.miaosha_6.redis.RedisService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: lianpengfei
 * @version: 1.0
 * @create: 2018/11/25
 */
@Service
public class MQSender {

    private static final Logger LOGGER = LogManager.getLogger(MQSender.class);

    @Autowired
    AmqpTemplate amqpTemplate;

    public void sendMiaoshaMessage(MiaoshaMessage miaoshaMessage) {
        String msg = RedisService.beanToString(miaoshaMessage);
        LOGGER.info("发送秒杀商品入队列:" + msg);
        amqpTemplate.convertAndSend(MQConfig.MIAOSHA_QUEUE,msg);
    }

//    public void send(Object message) {
//        String msg = RedisService.beanToString(message);
//        LOGGER.info("发送消息:" + msg);
//        amqpTemplate.convertAndSend(MQConfig.QUEUE, msg);
//    }
//
//
//    public void topicSend1(Object message) {
//        String msg = RedisService.beanToString(message);
//        LOGGER.info("发送消息:" + msg);
//        /**
//         * 发送时指定交换机名，routingKey为topic.key1，消息为msg
//         */
//        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key1", msg);
//    }
//
//    public void topicSend2(Object message) {
//        String msg = RedisService.beanToString(message);
//        LOGGER.info("发送消息:" + msg);
//        /**
//         * 发送时指定交换机名，routingKey为topic.key2，消息为msg
//         */
//        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key2", msg);
//    }
//
//    public void fanoutSned(Object message) {
//        String msg = RedisService.beanToString(message);
//        LOGGER.info("fanout发送消息:" + msg);
//        /**
//         * 广播模式发送消息时不需要指定routingKey
//         */
//        amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE, "", msg);
//    }
//
//    public void headerSend(Object message) {
//        String msg = RedisService.beanToString(message);
//        LOGGER.info("header发送消息:" + msg);
//
//        /**
//         * Header模式发送消息时，不需要指定routingKey，但是msg需要Message对象，Message对象需要接受字节数组以及一组头部信息，用于匹配
//         */
//        MessageProperties messageProperties = new MessageProperties();
//        messageProperties.setHeader("key1", "value1");
//        messageProperties.setHeader("key2", "value2");
//        Message obj = new Message(msg.getBytes(), messageProperties);
//        amqpTemplate.convertAndSend(MQConfig.HEADER_EXCHANGE, "", obj);
//    }
}
