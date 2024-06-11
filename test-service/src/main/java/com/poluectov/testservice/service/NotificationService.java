package com.poluectov.testservice.service;

import com.poluectov.testservice.model.KafkaNotificationMessage;

public interface NotificationService {

    void sendNotification(KafkaNotificationMessage notificationRequest);

}
