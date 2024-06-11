package com.poluectov.subscriptionservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscribedUserResponseDto {

    Long id;
    String username;
    String email;
    long subscriptionsCount;

}
