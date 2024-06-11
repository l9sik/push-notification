package com.poluectov.notificationservice.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.poluectov.notificationservice.model.FirebaseNotificationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FirebaseNotificationMessageServiceImpl implements NotificationMessageService {

    private final FirebaseMessaging firebaseMessaging;

    private static final int MAX_RETRIES = 3;

    /**
     * Sends a Firebase notification message.
     *
     * @param  notificationMessage   the Firebase notification message to be sent
     * @throws RuntimeException     if the maximum number of retries is reached
     */
    public void postNotification(FirebaseNotificationMessage notificationMessage){
        Message message = mapToMessage(notificationMessage);

        for (int i = 0; i < MAX_RETRIES; i++){
            try{
                firebaseMessaging.send(message);
                return;
            } catch (FirebaseMessagingException e) {
                log.error("Error sending notification", e);
                if (i == MAX_RETRIES - 1){
                    throw new RuntimeException("Error sending notification", e);
                }
            }
        }
    }

    private Message mapToMessage(FirebaseNotificationMessage notificationMessage) {
        Notification notification = Notification.builder()
                .setTitle(notificationMessage.getTitle())
                .setBody(notificationMessage.getBody())
                .setImage(notificationMessage.getImage())
                .build();

        return Message.builder()
                .setToken(notificationMessage.getRecipientToken())
                .setNotification(notification)
                .putAllData(notificationMessage.getData())
                .build();
    }
}
