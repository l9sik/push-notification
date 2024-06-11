package com.poluectov.notificationservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FirebaseNotificationMessage {
    String recipientToken;
    String title;
    String body;
    String image;
    Map<String, String> data;
}