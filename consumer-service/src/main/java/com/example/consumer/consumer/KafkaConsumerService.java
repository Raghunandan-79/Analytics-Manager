package com.example.consumer.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.MeterRegistry;

@Service
public class KafkaConsumerService {
    private final MeterRegistry registry;

    public KafkaConsumerService(MeterRegistry registry) {
        this.registry = registry;
    }

    @KafkaListener(topics = "testy", groupId = "metrics-consumer-group")
    public void consume(String event) {
        registry
            .counter("user_events_total", "event", event)
            .increment();

        System.out.println("Consumed event: " + event);
    }
}
