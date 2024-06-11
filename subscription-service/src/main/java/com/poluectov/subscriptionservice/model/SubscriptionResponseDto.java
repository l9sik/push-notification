package com.poluectov.subscriptionservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionResponseDto {
    Long id;
    Long userId;
    String recipientToken;
}
