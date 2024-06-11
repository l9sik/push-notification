package com.poluectov.notificationservice.service;

import com.poluectov.notificationservice.model.FirebaseNotificationMessage;

public interface NotificationMessageService {
    void postNotification(FirebaseNotificationMessage notificationMessage);
}
