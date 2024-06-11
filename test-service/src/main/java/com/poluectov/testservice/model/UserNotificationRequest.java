package com.poluectov.testservice.model;

import lombok.*;

import java.util.Map;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserNotificationRequest {


    Long userId;
    String title;
    String body;
    String imageUrl;
    Map<String, String> data;
}
