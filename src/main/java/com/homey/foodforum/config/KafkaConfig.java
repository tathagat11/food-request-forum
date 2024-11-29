package com.homey.foodforum.config;

import java.util.Properties;

public class KafkaConfig {
    public static Properties getProducerProperties() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("batch.size", "1");
        props.put("linger.ms", "0");
        props.put("bootstrap.servers", "localhost:9092");   
        return props;
    }

    public static Properties getConsumerProperties(String groupId) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", groupId);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("max.poll.interval.ms", "1000");
        props.put("auto.commit.interval.ms", "100");
        props.put("fetch.min.bytes", "1");
        props.put("bootstrap.servers", "localhost:9092");
        return props;
    }
}
