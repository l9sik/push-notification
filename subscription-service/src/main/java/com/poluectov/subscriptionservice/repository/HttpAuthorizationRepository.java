package com.poluectov.subscriptionservice.repository;

import com.poluectov.subscriptionservice.model.AuthenticationRequestDto;
import com.poluectov.subscriptionservice.model.AuthenticationResponseDto;
import com.poluectov.subscriptionservice.repository.AuthorizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HttpAuthorizationRepository implements AuthorizationRepository {

    private final WebClient.Builder webClientBuilder;

    @Override
    public Optional<AuthenticationResponseDto> find(String email) {

        AuthenticationRequestDto authenticationRequestDto = AuthenticationRequestDto.builder()
                .userEmail(email)
                .build();
        try {
            Optional<AuthenticationResponseDto> response = webClientBuilder.build()
                    .post()
                    .uri("lb://user-service/account/auth")
                    .body(Mono.just(authenticationRequestDto), AuthenticationRequestDto.class)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, resp -> Mono.empty())
                    .bodyToMono(AuthenticationResponseDto.class)
                    .blockOptional();
            if (response.isPresent()){
                if (response.get().getUserEmail() == null) {
                    return Optional.empty();
                }
            }

            return response;
        }catch(Exception e){
            return Optional.empty();
        }

    }
}
