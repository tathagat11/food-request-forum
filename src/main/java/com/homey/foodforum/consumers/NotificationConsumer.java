package com.homey.foodforum.consumers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homey.foodforum.config.KafkaConfig;
import com.homey.foodforum.events.ChefNotificationEvent;

public class NotificationConsumer {
    private final KafkaConsumer<String, String> consumer;
    private final ObjectMapper objectMapper;
    private static final String TOPIC = "chef-notifications";
    private volatile boolean running = true;
    // Store notifications per chef
    private Map<String, List<ChefNotificationEvent>> chefNotifications = new ConcurrentHashMap<>();
 
    public NotificationConsumer() {
        this.consumer = new KafkaConsumer<>(KafkaConfig.getConsumerProperties("notification-group"));
        this.objectMapper = new ObjectMapper();
        this.consumer.subscribe(Collections.singletonList(TOPIC));
    }
 
    public void consume() {
        try {
            while (running) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    ChefNotificationEvent event = objectMapper.readValue(record.value(), ChefNotificationEvent.class);
                    processNotification(event);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            consumer.close();
        }
    }
 
    private void processNotification(ChefNotificationEvent event) {
        chefNotifications.computeIfAbsent(event.getChefId(), k -> new ArrayList<>())
            .add(event);
        System.out.println("Chef " + event.getChefId() + " notified about request " + 
            event.getRequestId() + " matching tags: " + event.getMatchedTags());
    }
 
    public List<ChefNotificationEvent> getChefNotifications(String chefId) {
        return chefNotifications.getOrDefault(chefId, new ArrayList<>());
    }
 
    public void stop() {
        running = false;
    }
 }
