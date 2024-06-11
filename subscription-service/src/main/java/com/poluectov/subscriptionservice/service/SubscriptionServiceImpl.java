package com.poluectov.subscriptionservice.service;

import com.poluectov.subscriptionservice.model.*;
import com.poluectov.subscriptionservice.repository.AuthorizationRepository;
import com.poluectov.subscriptionservice.repository.SubscriptionRepository;
import com.poluectov.subscriptionservice.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService{

    private final SubscriptionRepository repository;
    private final AuthorizationRepository authenticationService;
    private final UserRepository userRepository;

    @Override
    public SubscriptionResponseDto create(SubscriptionRequestDto requestDto) {
        Subscription subscription = repository.save(mapToSubscription(requestDto));

        return mapToSubscriptionResponseDto(subscription);
    }

    @Override
    public SubscriptionResponseDto create(SubscriptionRequestDto subscriptionRequestDto, String email) {
        AuthenticationResponseDto user = authenticationService.find(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        subscriptionRequestDto.setUserId(user.getUserId());

        return create(subscriptionRequestDto);
    }

    @Override
    public Optional<SubscriptionResponseDto> one(Long id) {

        Optional<Subscription> subscription = repository.findById(id);

        return subscription.flatMap(
                s -> Optional.ofNullable(mapToSubscriptionResponseDto(s))
        );
    }

    @Override
    public List<SubscriptionResponseDto> findByUserId(Long id) {

        List<Subscription> subscriptions = repository.findByUserId(id);

        return subscriptions.stream().map(this::mapToSubscriptionResponseDto).toList();
    }

    @Override
    public List<SubscriptionResponseDto> findByEmail(String email) {
        AuthenticationResponseDto user = authenticationService.find(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return findByUserId(user.getUserId());
    }

    @Override
    public List<SubscriptionResponseDto> all() {
        return repository.findAll().stream().map(this::mapToSubscriptionResponseDto).toList();
    }

    @Override
    public List<SubscribedUserResponseDto> subscribedUsers() {

        // get all users id that have subscriptions
        List<Subscription> subscriptions = repository.findAll();
        Set<Long> users = subscriptions.stream().map(Subscription::getUserId).collect(Collectors.toSet());

        // get all subscribed users
        List<SubscribedUser> subscribedUsers = userRepository.subscribedUsers(users);

        return subscribedUsers.stream()
                .map(subscribedUser -> SubscribedUserResponseDto.builder()
                        .id(subscribedUser.getId())
                        .username(subscribedUser.getLogin())
                        .email(subscribedUser.getEmail())
                        //count subscriptions of that user
                        .subscriptionsCount(subscriptions.stream()
                                .filter(s ->
                                        Objects.equals(s.getUserId(), subscribedUser.getId()))
                                .count())
                        .build()
                )
                .toList();
    }

    private Subscription mapToSubscription(SubscriptionRequestDto requestDto){

        return Subscription.builder()
                .userId(requestDto.getUserId())
                .recipient(requestDto.getRecipientToken())
                .build();
    }

    private SubscriptionResponseDto mapToSubscriptionResponseDto(Subscription subscription){
        if (subscription == null)
            return null;
        return SubscriptionResponseDto.builder()
                .id(subscription.getId())
                .userId(subscription.getUserId())
                .recipientToken(subscription.getRecipient())
                .build();
    }
}
