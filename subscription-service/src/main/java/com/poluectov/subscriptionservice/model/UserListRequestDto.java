package com.poluectov.subscriptionservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserListRequestDto {
    List<Long> userIds;
}
