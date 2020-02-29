package com.yaoyao.k02_partitioner;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * 消息生产者-自定义分区器
 *
 * Created by yaoyao on 2020-02-28.
 */
public class ProducerDefinePartitioner {

    private static final String brokerList = "10.128.128.14:9092";

    private static final String topic = "yaoyaoTopic";

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.RETRIES_CONFIG, 10);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);

        //自定义分区器。如果未指定，就会使用默认分区策略：org.apache.kafka.clients.producer.internals.DefaultPartitioner
        properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, DefinePartitioner.class.getName());

        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);
        ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, "kafka-demo", "hello, 自定义分区器");
        try {
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
