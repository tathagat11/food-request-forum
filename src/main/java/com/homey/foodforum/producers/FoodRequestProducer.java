package com.homey.foodforum.producers;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homey.foodforum.config.KafkaConfig;
import com.homey.foodforum.events.FoodRequestEvent;

public class FoodRequestProducer {
    private final KafkaProducer<String, String> producer;
    private final ObjectMapper objectMapper;
    private static final String TOPIC = "food-requests";

    public FoodRequestProducer() {
        this.producer = new KafkaProducer<>(KafkaConfig.getProducerProperties());
        this.objectMapper = new ObjectMapper();
    }

    public void sendFoodRequest(FoodRequestEvent event) {
        try {
            String value = objectMapper.writeValueAsString(event);
            ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, event.getEventId(), value);
            producer.send(record);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        producer.close();
    }
}