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
 * @version 2023.0.1 2023/9/15:17:08
 * @since 2023.0.1
 */
public class CustomProducerParameters {

	public static void main(String[] args) {
		// 1. 创建 kafka 生产者的配置对象
		Properties properties = new Properties();
		// 2. 给 kafka 配置对象添加配置信息:bootstrap.servers
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"cluster001:9092");
		// key,value 序列化
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

		// batch.size:批次大小，默认16K
		properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
		// linger.ms:等待时间，默认 0
		properties.put(ProducerConfig.LINGER_MS_CONFIG, 1);
		// RecordAccumulator:缓冲区大小，默认 32M:buffer.memory
		properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG,33554432);
//		// 设置应答级别
//		properties.put(ProducerConfig.ACKS_CONFIG,"all");
//		// 设置重试次数
//		properties.put(ProducerConfig.RETRIES_CONFIG,1);
		// compression.type:压缩，默认 none，可配置值 gzip、snappy、 lz4 和 zstd
		properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG,"snappy");


		// 3. 创建 kafka 生产者对象
		KafkaProducer<String,String> kafkaProducer = new KafkaProducer<>(properties);

		// 4. 调用 send 方法,向 first 主题发送消息
		for (int i = 0; i < 5; i++) {
			kafkaProducer.send(new ProducerRecord<>("sample-partition","jcohy " + i),
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
