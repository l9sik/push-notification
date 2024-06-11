package com.poluectov.testservice.service.impl;

import com.poluectov.testservice.config.WebClientConfig;
import com.poluectov.testservice.model.KafkaNotificationMessage;
import com.poluectov.testservice.model.Subscription;
import com.poluectov.testservice.model.SubscriptionResponseDto;
import com.poluectov.testservice.model.UserNotificationRequest;
import com.poluectov.testservice.service.NotificationService;
import com.poluectov.testservice.service.SubscriptionService;
import com.poluectov.testservice.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final SubscriptionService subscriptionService;
    private final NotificationService notificationService;

    @Override
    public void sendNotification(UserNotificationRequest notificationRequest) {
        //send request and wait for response (via http request) to subscription service
        List<SubscriptionResponseDto> subscriptions = subscriptionService.getSubscriptions(
                notificationRequest.getUserId()
        );
        //iterate over subscriptions and send notification to kafka topic
        for (SubscriptionResponseDto subscription : subscriptions) {
            notificationService.sendNotification(
                    KafkaNotificationMessage.builder()
                            .recipientToken(subscription.getRecipientToken())
                            .title(notificationRequest.getTitle())
                            .body(notificationRequest.getBody())
                            .image(notificationRequest.getImageUrl())
                            .data(notificationRequest.getData())
                            .build()
            );
        }
    }
}
