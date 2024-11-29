package com.homey.foodforum.consumers;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homey.foodforum.config.KafkaConfig;
import com.homey.foodforum.events.ChefNotificationEvent;
import com.homey.foodforum.events.FoodRequestEvent;
import com.homey.foodforum.models.Chef;
import com.homey.foodforum.producers.ChefNotificationProducer;

// consumers/TagMatchingConsumer.java
public class TagMatchingConsumer {
    private final KafkaConsumer<String, String> consumer;
    private final ChefNotificationProducer notificationProducer;
    private final ObjectMapper objectMapper;
    private final Map<String, Chef> chefs;
    private static final String TOPIC = "food-requests";
    private volatile boolean running = true;
 
    public TagMatchingConsumer() {
        this.consumer = new KafkaConsumer<>(KafkaConfig.getConsumerProperties("tag-matching-group"));
        this.notificationProducer = new ChefNotificationProducer();
        this.objectMapper = new ObjectMapper();
        this.consumer.subscribe(Collections.singletonList(TOPIC));
        
        // Hardcoded chefs
        this.chefs = new HashMap<>();
        chefs.put("chef1", new Chef("chef1", "John", Arrays.asList("indian", "vegetarian", "spicy")));
        chefs.put("chef2", new Chef("chef2", "Mike", Arrays.asList("chinese", "seafood")));
        chefs.put("chef3", new Chef("chef3", "Lisa", Arrays.asList("italian", "pizza", "pasta")));
    }
 
    public void consume() {
        try {
            while (running) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    FoodRequestEvent event = objectMapper.readValue(record.value(), FoodRequestEvent.class);
                    processRequest(event);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            consumer.close();
            notificationProducer.close();
        }
    }
 
    private void processRequest(FoodRequestEvent requestEvent) {
        chefs.values().forEach(chef -> {
            List<String> matchedTags = chef.getTags().stream()
                .filter(tag -> requestEvent.getTags().contains(tag))
                .collect(Collectors.toList());
            
            if (!matchedTags.isEmpty()) {
                ChefNotificationEvent notificationEvent = new ChefNotificationEvent(
                    UUID.randomUUID().toString(),
                    "CHEF_NOTIFICATION",
                    System.currentTimeMillis(),
                    chef.getId(),
                    requestEvent.getRequestId(),
                    matchedTags,
                    requestEvent.getDishName() + ": " + requestEvent.getDescription()
                );
                notificationProducer.sendNotification(notificationEvent);
            }
        });
    }
 
    public void stop() {
        running = false;
    }
 }
