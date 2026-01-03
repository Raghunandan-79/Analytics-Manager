package com.example.producer.controller;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/producer")
public class ProducerController {
    private static final String TOPIC = "testy";
    private final KafkaProducer<String, String> producer;

    public ProducerController() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.70.128:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        this.producer = new KafkaProducer<>(props);
    }

    @PostMapping("/event")
    public void sendEvent(@RequestBody String event) {
        producer.send(new ProducerRecord<>(TOPIC, event));
        System.out.println("Produced event: " + event);
    }
}
