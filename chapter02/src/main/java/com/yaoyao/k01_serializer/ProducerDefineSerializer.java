package com.yaoyao.k01_serializer;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;

/**
 * 消息生产者-自定义序列化器
 *
 * Created by yaoyao on 2020-02-28.
 */
public class ProducerDefineSerializer {

    private static final String brokerList = "10.128.128.14:9092";

    private static final String topic = "yaoyaoTopic";

    public static void main(String[] args) {
        Properties properties = new Properties();

        //设置key序列化器
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, CompanySerializer.class.getName());
        //设置重试次数
        properties.put(ProducerConfig.RETRIES_CONFIG, 10);
        //设置值序列化器
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, CompanySerializer.class.getName());
        //设置集群地址
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);

        KafkaProducer<String, Company> producer = new KafkaProducer<String, Company>(properties);
        Company company = new Company();
        company.setName("小红");
        company.setAddress("自定义序列器");
        ProducerRecord<String, Company> record = new ProducerRecord<String, Company>(topic, company);
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
