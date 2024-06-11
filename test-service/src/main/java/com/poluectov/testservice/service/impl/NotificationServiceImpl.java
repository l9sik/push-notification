package com.poluectov.testservice.service.impl;

import com.poluectov.testservice.model.KafkaNotificationMessage;
import com.poluectov.testservice.sender.KafkaNotificationSender;
import com.poluectov.testservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final KafkaNotificationSender kafkaNotificationSender;

    private final String notificationRequestTopic;

    public NotificationServiceImpl(
            KafkaNotificationSender kafkaNotificationSender,
            @Value("${kafka.topic.notification.request}") String notificationRequestTopic) {
        this.kafkaNotificationSender = kafkaNotificationSender;
        this.notificationRequestTopic = notificationRequestTopic;
    }
    @Override
    public void sendNotification(KafkaNotificationMessage notificationRequest) {
        kafkaNotificationSender.send( notificationRequestTopic, notificationRequest );
    }
}
