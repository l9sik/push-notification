package com.poluectov.testservice.repository.impl;

import com.poluectov.testservice.model.Subscription;
import com.poluectov.testservice.model.SubscriptionResponseDto;
import com.poluectov.testservice.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Repository
@RequiredArgsConstructor
public class SubscriptionRepositoryImpl implements SubscriptionRepository {
    private final WebClient.Builder webClientBuilder;

    @Override
    public List<Subscription> findByUserId(Long userId) {

        Optional<Subscription[]> response = webClientBuilder.build()
                .get()
                .uri("lb://subscription-service/subscriptions/users/" + userId)
                .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT, APPLICATION_JSON_VALUE)
                .retrieve()
                .onStatus(HttpStatusCode::isError, resp -> Mono.empty())
                .bodyToMono(Subscription[].class)
                .blockOptional();

        return response.map(List::of).orElse(Collections.emptyList());
    }
}
