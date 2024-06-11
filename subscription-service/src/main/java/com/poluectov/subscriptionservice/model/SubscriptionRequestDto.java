package com.poluectov.subscriptionservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionRequestDto {
    Long userId;
    String recipientToken;
}
