package com.yaoyao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * kafaka：生产者、消费者
 *
 * Created by yaoyao on 2020-02-29.
 */
@RestController
public class KafkaController {

    private static final Logger logger = LoggerFactory.getLogger(KafkaController.class);

    @Autowired
    private KafkaTemplate template;

    private static final String topic = "yaoyaoTopic";

    @RequestMapping("/index")
    public String index() {
        return "hello, kafka!";
    }

//    //生产者
//    @RequestMapping("/send/{input}")
//    public String send(@PathVariable String input) {//如访问地址为/send/hehe，input的值就为hehe
//        template.send(topic, input + "-" + new Date().getTime());
//        return "生产者发送消息成功。" + input;
//    }

    //生产者：kafka的事务
    @RequestMapping("/send2/{input}")
    public String send2(@PathVariable String input) {
        //事务的支持
        template.executeInTransaction(t ->{
            t.send(topic, input + "-one");
            if ("error".equals(input)) {
                throw new RuntimeException("发送消息（事务）异常. input error");
            }
            t.send(topic, input + "-two");
            return true;
        });
        return "生产者（事务）发送消息成功。" + input;
    }

    //生产者：kafka的事务
    @Transactional //事务的支持
    @RequestMapping("/send3/{input}")
    public String send3(@PathVariable String input) {
        template.send(topic, input + "-one");
        if ("error".equals(input)) {
            throw new RuntimeException("发送消息（事务）异常. input error");
        }
        template.send(topic, input + "-two");
        return "生产者（事务）发送消息成功。" + input;
    }

    //消费者
    @KafkaListener(id = "receive1", topics = topic, groupId = "group.demo")
    public void listener(String input) {
        logger.info("接收到消息group.demo：{}", input);
    }

    //消费者
    @KafkaListener(id = "receive2", topics = topic, groupId = "group.demo2")
    public void listener3(String input) {
        logger.info("接收到消息group.demo2：{}", input);
    }

}
