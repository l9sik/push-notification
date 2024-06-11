package com.poluectov.notificationservice.listeners;

import com.poluectov.notificationservice.model.FirebaseNotificationMessage;
import com.poluectov.notificationservice.service.NotificationMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationRequestListener {

    private final NotificationMessageService notificationService;

    @KafkaListener(topics = "${kafka.topic.notification.request}")
    public void notificationListener(FirebaseNotificationMessage notificationMessage){
        notificationService.postNotification(notificationMessage);
    }
}
