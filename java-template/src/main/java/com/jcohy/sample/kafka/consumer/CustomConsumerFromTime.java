package com.jcohy.sample.kafka.consumer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndTimestamp;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/10/31:14:25
 * @since 2023.0.1
 */
public class CustomConsumerFromTime {

	public static void main(String[] args) {
		// 1.创建消费者的配置对象
		Properties properties = new Properties();
		// 2.给消费者配置对象添加参数
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "cluster001:9092");
		// 配置序列化 必须
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

		// 配置消费者组（组名任意起名） 必须
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, "test");

		// 创建消费者对象
		KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
		// 注册要消费的主题（可以消费多个主题）
		ArrayList<String> topics = new ArrayList<>();
		topics.add("first");
		kafkaConsumer.subscribe(topics);

		Set<TopicPartition> assignment = new HashSet<>();
		while (assignment.size() == 0) {
			kafkaConsumer.poll(Duration.ofSeconds(1));
			// 获取消费者分区分配信息（有了分区分配信息才能开始消费）
			assignment = kafkaConsumer.assignment();
		}
		HashMap<TopicPartition, Long> timestampToSearch = new HashMap<>();
		// 封装集合存储，每个分区对应一天前的数据
		for (TopicPartition topicPartition : assignment) {
			timestampToSearch.put(topicPartition,
					System.currentTimeMillis() - 1 * 24 * 3600 * 1000);
		}
		// 获取从1天前开始消费的每个分区的 offset
		Map<TopicPartition, OffsetAndTimestamp> offsets =
				kafkaConsumer.offsetsForTimes(timestampToSearch);
		// 遍历每个分区，对每个分区设置消费时间。
		for (TopicPartition topicPartition : assignment) {
			OffsetAndTimestamp offsetAndTimestamp =
					offsets.get(topicPartition);
			// 根据时间指定开始消费的位置
			if (offsetAndTimestamp != null){
				kafkaConsumer.seek(topicPartition,
						offsetAndTimestamp.offset());
			}
		}

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
			// 同步提交offset
			kafkaConsumer.commitAsync();
		}
	}
}
