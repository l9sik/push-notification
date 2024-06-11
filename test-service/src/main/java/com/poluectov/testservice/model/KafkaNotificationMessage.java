package com.poluectov.testservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KafkaNotificationMessage {
    String recipientToken;
    String title;
    String body;
    String image;
    Map<String, String> data;
}