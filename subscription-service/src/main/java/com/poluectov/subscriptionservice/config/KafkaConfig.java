package com.poluectov.subscriptionservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Value("${kafka.url}")
    String kafkaUrl;

    @Value("${kafka.topic.subscription.request}")
    String notificationRequestTopic;

    @Bean
    public NewTopic requestTopic() {
        return TopicBuilder.name(notificationRequestTopic)
                .partitions(10)
                .replicas(1)
                .build();
    }
}