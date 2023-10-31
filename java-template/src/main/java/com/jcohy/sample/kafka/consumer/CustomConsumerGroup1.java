package com.jcohy.sample.kafka.consumer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/10/30:16:55
 * @since 2023.0.1
 */
public class CustomConsumerGroup1 {

	public static void main(String[] args) {
		// 1.创建消费者的配置对象
		Properties properties = new Properties();
		// 2.给消费者配置对象添加参数
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "cluster001:9092");
		// 配置序列化 必须
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

		// 修改分区分配策略
		properties.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, "org.apache.kafka.clients.consumer.StickyAssignor");
		// 配置消费者组 必须
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, "test3");

		// 创建消费者对象
		KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<String, String>(properties);
		// 注册主题
		ArrayList<String> topics = new ArrayList<>();
		topics.add("first");
		kafkaConsumer.subscribe(topics);
		// 拉取数据打印
		while (true) {
			// 设置1s中消费一批数据
			ConsumerRecords<String, String> consumerRecords =
					kafkaConsumer.poll(Duration.ofSeconds(1));
			// 打印消费到的数据
			for (ConsumerRecord<String, String> consumerRecord :
					consumerRecords) {
				System.out.println(consumerRecord);
			}
		}
	}
}
