package com.poluectov.subscriptionservice.service;

import com.poluectov.subscriptionservice.model.SubscribedUserResponseDto;
import com.poluectov.subscriptionservice.model.SubscriptionRequestDto;
import com.poluectov.subscriptionservice.model.SubscriptionResponseDto;

import java.util.List;
import java.util.Optional;

public interface SubscriptionService {

    SubscriptionResponseDto create(SubscriptionRequestDto requestDto);

    SubscriptionResponseDto create(SubscriptionRequestDto subscriptionRequestDto, String email);

    Optional<SubscriptionResponseDto> one(Long id);

    List<SubscriptionResponseDto> findByUserId(Long id);

    List<SubscriptionResponseDto> findByEmail(String email);

    List<SubscriptionResponseDto> all();

    List<SubscribedUserResponseDto> subscribedUsers();

}
