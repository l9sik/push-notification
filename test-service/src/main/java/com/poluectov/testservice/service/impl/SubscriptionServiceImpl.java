package com.poluectov.testservice.service.impl;

import com.poluectov.testservice.model.Subscription;
import com.poluectov.testservice.model.SubscriptionResponseDto;
import com.poluectov.testservice.repository.SubscriptionRepository;
import com.poluectov.testservice.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    @Override
    public List<SubscriptionResponseDto> getSubscriptions(Long userId) {
        return subscriptionRepository.findByUserId(userId).stream()
                .map(
                        this::mapToSubscriptionResponseDto
                ).toList();
    }

    private SubscriptionResponseDto mapToSubscriptionResponseDto(Subscription subscription) {
        if (subscription == null)
            return null;
        return SubscriptionResponseDto.builder()
                .recipientToken(subscription.getRecipientToken())
                .build();
    }
}
