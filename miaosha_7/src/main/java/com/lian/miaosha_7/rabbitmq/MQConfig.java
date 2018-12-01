package com.lian.miaosha_7.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: lianpengfei
 * @version: 1.0
 * @create: 2018/11/25
 */
@Configuration
public class MQConfig {

    public static final String MIAOSHA_QUEUE = "miaosha.queue";
    public static final String QUEUE = "queue";
    public static final String TOPIC_QUEUE1 = "topic.queue1";
    public static final String TOPIC_QUEUE2 = "topic.queue2";
    public static final String HEADER_QUEUE = "header.queue";
    public static final String TOPIC_EXCHANGE = "topicExchange";
    public static final String FANOUT_EXCHANGE = "fanoutExchange";
    public static final String HEADER_EXCHANGE = "headerExchange";

    @Bean
    public Queue queue(){
        /**
         * name:队列名
         * durable：是否做持久化
         */
        return new Queue(QUEUE,true);
    }

    @Bean
    public Queue miaoshaQueu(){
        return new Queue(MIAOSHA_QUEUE,true);
    }

    @Bean
    public Queue topicQueue1(){
        return new Queue(TOPIC_QUEUE1,true);
    }

    @Bean
    public Queue topicQueue2(){
        return new Queue(TOPIC_QUEUE2,true);
    }


    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(TOPIC_EXCHANGE);
    }


    /**
     * 将topicQueue1队列与交换机topicExchange绑定，routingKey为topic.key1
     * @return
     */
    @Bean
    public Binding topicBinding1(){
        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with("topic.key1");
    }

    /**
     * 将topicQueue2队列与交换机topicExchange绑定，routingKey为topic.#
     * @return
     */
    @Bean
    public Binding topicBinding2(){
        return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with("topic.#");
    }

    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(FANOUT_EXCHANGE);
    }

    @Bean
    public Binding fanoutBinding1(){
        /**
         * 广播模式绑定队列和交换机时，不需要指定routingKey
         */
        return BindingBuilder.bind(topicQueue1()).to(fanoutExchange());
    }

    @Bean
    public Binding fanoutBinding2(){
        /**
         * 广播模式绑定队列和交换机时，不需要指定routingKey
         */
        return BindingBuilder.bind(topicQueue2()).to(fanoutExchange());
    }

    @Bean
    public Queue headerQueue(){
        return new Queue(HEADER_QUEUE,true);
    }

    @Bean
    public HeadersExchange headersExchange(){
        return new HeadersExchange(HEADER_EXCHANGE);
    }

    @Bean
    public Binding headersBinding(){

        /**
         * Header模式与Topic模式类似，区别在于：Topic模式通过routingKey来routingKey来绑定交换机和队列，Header模式通过Map对象绑定交换机和队列,只有当map匹配时，才发送
         */
        Map<String,Object> headersValue = new HashMap<>();
        headersValue.put("key1","value1");
        headersValue.put("key2","value2");
        return BindingBuilder.bind(headerQueue()).to(headersExchange()).whereAll(headersValue).match();
    }
}
