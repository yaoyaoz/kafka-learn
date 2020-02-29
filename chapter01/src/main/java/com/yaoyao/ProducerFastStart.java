package com.yaoyao;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * kafka 消息生产者
 *
 * Created by yaoyao on 2020-02-28.
 */
public class ProducerFastStart {

    /*
    如果报错：java.io.IOException: Can't resolve address: rs-test-yzy04.lo-cq-kylin01.host.cloudwalk.work:9092，
    可以在hosts文件映射启动kafka那台机子：10.128.128.14 rs-test-yzy04.lo-cq-kylin01.host.cloudwalk.work

    linux:查看防火墙状态（要关闭才能访问过去）：firewall-cmd --state
     */
    private static final String brokerList = "10.128.128.14:9092";

    private static final String topic = "yaoyaoTopic";

    public static void main(String[] args) {
        Properties properties = new Properties();

        //设置key序列化器
//        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        //设置重试次数
        properties.put(ProducerConfig.RETRIES_CONFIG, 10);

        //设置值序列化器
//        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        //设置集群地址
//        properties.put("bootstrap.servers", brokerList);
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);

        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);
        ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, "kafka-demo", "hello, kafka");
        try {
            /*
            同步发送：producer.send(record)
             */
//            Future<RecordMetadata> send = producer.send(record);
//            RecordMetadata recordMetadata = send.get();
//            System.out.println("topic:" + recordMetadata.topic());
//            System.out.println("partition:" + recordMetadata.partition());
//            System.out.println("offset:" + recordMetadata.offset());

            /*
            异步发送
             */
            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if (exception == null) {
                        System.out.println("异步发送消息回调：" + metadata.partition() + ":" + metadata.offset());
                    } else {
                        System.out.println("异步发送消息异常：" + exception);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        producer.close();

    }

}
