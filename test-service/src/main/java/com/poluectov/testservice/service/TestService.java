package com.poluectov.testservice.service;

import com.poluectov.testservice.model.UserNotificationRequest;

public interface TestService {

    void sendNotification(UserNotificationRequest notificationRequest);

}
