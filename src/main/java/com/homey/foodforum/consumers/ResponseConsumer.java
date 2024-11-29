package com.homey.foodforum.consumers;

import java.time.Duration;
import java.util.Collections;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homey.foodforum.config.KafkaConfig;
import com.homey.foodforum.events.ResponseEvent;

// ResponseConsumer.java
public class ResponseConsumer {
    private final KafkaConsumer<String, String> consumer;
    private final ObjectMapper objectMapper;
    private static final String TOPIC = "chef-responses";
    private volatile boolean running = true;
 
    public ResponseConsumer() {
        this.consumer = new KafkaConsumer<>(KafkaConfig.getConsumerProperties("response-group"));
        this.objectMapper = new ObjectMapper();
        this.consumer.subscribe(Collections.singletonList(TOPIC));
    }
 
    public void consume() {
        try {
            while (running) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    ResponseEvent event = objectMapper.readValue(record.value(), ResponseEvent.class);
                    System.out.println("Received response: " + event);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            consumer.close();
        }
    }
 
    public void stop() {
        running = false;
    }
 }
