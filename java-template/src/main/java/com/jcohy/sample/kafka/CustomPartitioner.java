package com.jcohy.sample.kafka;

import java.util.Map;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/9/15:16:58
 * @since 2023.0.1
 */
public class CustomPartitioner implements Partitioner {
	@Override
	public void close() {

	}

	@Override
	public void configure(Map<String, ?> configs) {

	}

	/**
	 * 返回信息对应的分区
	 *
	 * @param topic 主题
	 * @param key 消息的 key
	 * @param keyBytes 消息的 key 序列化后的字节数组
	 * @param value 消息的 value
	 * @param valueBytes 消息的 value 序列化后的字节数组
	 * @param cluster 集群元数据可以查看分区信息
	 * @return /
	 */
	@Override
	public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
		// 获取消息
		String msgValue = value.toString();
		int partition;
		// 判断消息是否包含 jcohy
		if (msgValue.contains("jcohy")){
			partition = 0;
		}else {
			partition = 1;
		}
		// 返回分区号
		return partition;
	}
}
