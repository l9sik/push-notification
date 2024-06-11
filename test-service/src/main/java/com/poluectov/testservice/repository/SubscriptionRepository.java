package com.poluectov.testservice.repository;

import com.poluectov.testservice.model.Subscription;

import java.util.List;

public interface SubscriptionRepository {

    List<Subscription> findByUserId(Long userId);
}
