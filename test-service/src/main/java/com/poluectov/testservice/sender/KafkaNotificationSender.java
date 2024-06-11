package com.poluectov.testservice.sender;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poluectov.testservice.model.KafkaNotificationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaNotificationSender {

    @Value("${kafka.topic.message.request}")
    public static String MESSAGE_REQUEST_TOPIC;

    public static Long timeout = 1000L;
    ObjectMapper mapper = new ObjectMapper();

    private final KafkaTemplate<String, KafkaNotificationMessage> template;

    public KafkaNotificationSender(KafkaTemplate<String, KafkaNotificationMessage> template) {
        this.template = template;
    }

    public void send(String topic, KafkaNotificationMessage message) {

        log.info("Sending message: {}", message);

        Message<KafkaNotificationMessage> msg = MessageBuilder
                .withPayload(message)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .build();

        template.send(msg);
    }
}
