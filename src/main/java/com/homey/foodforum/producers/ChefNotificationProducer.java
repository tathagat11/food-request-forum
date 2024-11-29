package com.homey.foodforum.producers;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homey.foodforum.config.KafkaConfig;
import com.homey.foodforum.events.ChefNotificationEvent;

public class ChefNotificationProducer {
    private final KafkaProducer<String, String> producer;
    private final ObjectMapper objectMapper;
    private static final String TOPIC = "chef-notifications";

    public ChefNotificationProducer() {
        this.producer = new KafkaProducer<>(KafkaConfig.getProducerProperties());
        this.objectMapper = new ObjectMapper();
    }

    public void sendNotification(ChefNotificationEvent event) {
        try {
            String value = objectMapper.writeValueAsString(event);
            producer.send(new ProducerRecord<>(TOPIC, event.getEventId(), value));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        producer.close();
    }
}
