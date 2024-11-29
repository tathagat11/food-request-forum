package com.homey.foodforum.consumers;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homey.foodforum.config.KafkaConfig;
import com.homey.foodforum.events.FoodRequestEvent;

import java.time.Duration;
import java.util.Collections;

public class FoodRequestConsumer {
    private final KafkaConsumer<String, String> consumer;
    private final ObjectMapper objectMapper;
    private static final String TOPIC = "food-requests";
    private volatile boolean running = true;

    public void stop() {
        running = false;
    }

    public FoodRequestConsumer() {
        this.consumer = new KafkaConsumer<>(KafkaConfig.getConsumerProperties("food-request-group"));
        this.objectMapper = new ObjectMapper();
        this.consumer.subscribe(Collections.singletonList(TOPIC));
    }

    public void consume() {
        try {
            while (running) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    FoodRequestEvent event = objectMapper.readValue(record.value(), FoodRequestEvent.class);
                    System.out.println("Received event: " + event);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            consumer.close();
        }
    }
}