package com.poluectov.testservice.service;

import com.poluectov.testservice.model.Subscription;
import com.poluectov.testservice.model.SubscriptionResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SubscriptionService {

    List<SubscriptionResponseDto> getSubscriptions(Long userId);

}
