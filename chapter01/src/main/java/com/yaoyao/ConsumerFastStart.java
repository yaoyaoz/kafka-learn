package com.yaoyao;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.Set;

/**
 * kafka 消费端
 *
 * Created by yaoyao on 2020-02-28.
 */
public class ConsumerFastStart {

    private static final String brokerList = "10.128.128.14:9092";

    private static final String groupId = "group.demo";

    private static final String topic = "yaoyaoTopic";

    public static void main(String[] args) {
        Properties properties = new Properties();

//        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

//        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

//        properties.put("bootstrap.servers", brokerList);
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);

        //消费组：必须指定，否则会报错：The configured groupId is invalid
//        properties.put("group.id", groupId);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);
        consumer.subscribe(Collections.singleton(topic)); //通过subscribe()订阅主题
//        consumer.subscribe(Pattern.compile("test*")); //可以使用正则表达式接收主题
//        consumer.assign(Arrays.asList(new TopicPartition(topic, 0))); //可以指定订阅的分区

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

            Set<TopicPartition> assignment = consumer.assignment();
            System.out.println("获取消费者分配到的分区:" + assignment);

            for (ConsumerRecord<String, String> record : records) {
                System.out.println("接收到消息：" + record.partition() + ":" + record.offset() + " " + record.value());
            }
        }
    }

}
