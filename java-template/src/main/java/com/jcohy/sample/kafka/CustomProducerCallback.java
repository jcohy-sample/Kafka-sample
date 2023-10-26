package com.jcohy.sample.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/9/15:14:41
 * @since 2023.0.1
 */
public class CustomProducerCallback {
	public static void main(String[] args) {
		// 1. 创建 kafka 生产者的配置对象
		Properties properties = new Properties();
		// 2. 给 kafka 配置对象添加配置信息:bootstrap.servers
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"cluster001:9092");
		// key,value 序列化
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

		// 3. 创建 kafka 生产者对象
		KafkaProducer<String,String> kafkaProducer = new KafkaProducer<>(properties);

		// 4. 调用 send 方法,向 first 主题发送消息
		for (int i = 0; i < 5; i++) {
			kafkaProducer.send(new ProducerRecord<>("first","jcohy " + i),
					(metadata, exception) -> { // // 该方法在Producer收到ack时调用，为异步调用
				if(exception == null) {
					// 没有异常,输出信息到控制台
					System.out.println(" 主 题 : " +
							metadata.topic() + "->" + "分区:" + metadata.partition());
				} else {
					exception.printStackTrace();
				}

			});
		}
		// 5. 关闭资源
		kafkaProducer.close();
	}
}
