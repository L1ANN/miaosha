package com.lian.miaosha_6.controller;

import com.lian.miaosha_6.rabbitmq.MQSender;
import com.lian.miaosha_6.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: lianpengfei
 * @version: 1.0
 * @create: 2018/11/25
 */
@Controller
@RequestMapping("mq")
public class MQController {

    @Autowired
    MQSender mqSender;

//    @ResponseBody
//    @RequestMapping("/demo")
//    public Result demo(){
//        mqSender.send("hello,lian");
//
//        return Result.success("success");
//    }
//
//    @ResponseBody
//    @RequestMapping("/topicDemo1")
//    public Result topicDemo1(){
//        mqSender.topicSend1("topic_test");
//        return Result.success("topicSendSuccess");
//    }
//
//    @ResponseBody
//    @RequestMapping("/topicDemo2")
//    public Result topicDemo2(){
//        mqSender.topicSend2("topic_test");
//        return Result.success("topicSendSuccess");
//    }
//
//    @ResponseBody
//    @RequestMapping("/fanoutDemo")
//    public Result fanoutDemo(){
//        mqSender.fanoutSned("fanout_test");
//        return Result.success("fanoutSendSuccess");
//    }
//
//    @ResponseBody
//    @RequestMapping("/headerDemo")
//    public Result resultDemo(){
//        mqSender.headerSend("header_test");
//        return Result.success("headerSendSuccess");
//    }
}
