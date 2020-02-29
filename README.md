# 传智播客：视频-Kafka深入探秘者来了

## 第一章 初始Kafka

### 1、特性

高吞吐量、低延迟；

持久性、可靠性：持久化到本地磁盘；

高并发。

### 2、使用场景

日志收集

消息系统：解耦生产者和消费者、缓存消息

用户活动跟踪

运营指标

流式处理：比如sprak streaming和storm

### 3、环境配置

linux环境

jdk

zookeeper

kafka

### 4、消息的生产与消费

首先创建一个主题：`bin/kafka-topics.sh --zookeeper localhost:2181 --create --topic yaoyaoTopic --partitions 2 --replication-factor 1`

>--partitions：指定了分区个数
>--replication-factor：指定了副本因子

展示所有主题：`bin/kafka-topics.sh --zookeeper localhost:2181 --list`

查看主题详情：`bin/kafka-topics.sh --zookeeper localhost:2181 --describe --topic yaoyaoTopic`

> --describe：查看详情动作指令

启动消费端接收消息：`bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic yaoyaoTopic`，窗口不关，另开一个窗口启动生产端

>--bootstrap-server：指定了连接kafka集群的地址

生产端发送消息：`bin/kafka-console-producer.sh --broker-list localhost:9092 --topic yaoyaoTopic`，然后输入内容，消费端都会接收到

>--broker-list：指定了连接kafka集群的地址

### 5、Java第一个程序

通过java程序来进行kafka收发消息

防火墙、telnet ip port

## 第二章 生产者

### 1、自定义序列化器

com.yaoyao.k01_serializer

### 2、自定义分区器

com.yaoyao.k02_partitioner

### 3、拦截器

com.yaoyao.k03_interceptor

### 4、其他生产者参数

acks："0"、"1"、"-1"

retries

batch.size

max.request.size

max.request.size

## 第三章 消费者

位移

同步、异步提交（重复消费、消息丢失）

## 第四章 主题

## 第五章 分区

## 第六章 物理存储

kafka的Message存储采用了分区（partition）、分段（LogSement）和稀疏索引这几个手段来达到了高效性。

消息顺序追加、页缓存、零拷贝技术

## 第七章 稳定性

幂等性

事务

## 第八章 高级应用

## 第九章 集群管理

## 第十章 监控



