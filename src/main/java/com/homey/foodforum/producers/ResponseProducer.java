package com.homey.foodforum.producers;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homey.foodforum.config.KafkaConfig;
import com.homey.foodforum.events.ResponseEvent;

public class ResponseProducer {
    private final KafkaProducer<String, String> producer;
    private final ObjectMapper objectMapper;
    private static final String TOPIC = "chef-responses";

    public ResponseProducer() {
        this.producer = new KafkaProducer<>(KafkaConfig.getProducerProperties());
        this.objectMapper = new ObjectMapper();
    }

    public void sendResponse(ResponseEvent event) {
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
