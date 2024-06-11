package com.poluectov.subscriptionservice.repository;

import com.ctc.wstx.shaded.msv_core.util.Uri;
import com.poluectov.subscriptionservice.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HttpUserRepository implements UserRepository {

    private final WebClient.Builder webClientBuilder;

    @Override
    public List<SubscribedUser> subscribedUsers(Collection<Long> userIds) {
/*
        UriComponents uriComponents = UriComponentsBuilder
                .fromUriString("")
                .encode()
                .build(false);*/

        //url/some/1,2,3
        StringBuilder userIdsStringBuilder = new StringBuilder();
        userIds.forEach(userId -> userIdsStringBuilder.append(userId).append(","));
        userIdsStringBuilder.deleteCharAt(userIdsStringBuilder.length() - 1);
        String userIdsString = userIdsStringBuilder.toString();


        URI uri = URI.create("lb://user-service/users/some/" + userIdsString);
        try {
            Optional<SubscribedUser[]> response = webClientBuilder.build()
                    .get()
                    .uri(uri)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, resp -> Mono.empty())
                    .bodyToMono(SubscribedUser[].class)
                    .blockOptional();
            return response.map(
                        userListResponseDtos -> Arrays.stream(userListResponseDtos)
                        .toList()
                    )
                    .orElse(Collections.emptyList());

        }catch(Exception e){
            return Collections.emptyList();
        }
    }
}
