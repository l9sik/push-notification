package com.poluectov.authorizationserver.repository;

import com.poluectov.authorizationserver.exception.BadRequestException;
import com.poluectov.authorizationserver.exception.NotFoundException;
import com.poluectov.authorizationserver.exception.UserAlreadyExistsException;
import com.poluectov.authorizationserver.model.dto.*;

import com.poluectov.authorizationserver.exception.InternalServerErrorException;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Optional;

@Service
@AllArgsConstructor
public class HttpAuthorizationRepository implements AuthorizationRepository {
    WebClient.Builder webClientBuilder;

    @Override
    public Optional<AuthenticationResponseDto> find(AuthenticationRequestDto authenticationRequestDto) {
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

    @Override
    public RegisterResponseDto save(RegisterRequestDto newUser) {
        Optional<RegisterResponseDto> response = webClientBuilder.build()
                .post()
                .uri("lb://user-service/account/reg")
                .body(Mono.just(newUser), RegisterRequestDto.class)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        resp -> switch (resp.statusCode().value()) {
                    case 400 -> Mono.error(new BadRequestException("The request was invalid."));
                    case 404 -> Mono.error(new NotFoundException("The requested resource was not found."));
                    case 409 -> Mono.error(new UserAlreadyExistsException("User already exists"));
                    default -> Mono.error(new InternalServerErrorException("Internal server error"));}
                )
                .bodyToMono(RegisterResponseDto.class)
                .blockOptional();
        if (response.isEmpty()) {
            throw new UserAlreadyExistsException("User not found");
        }
        return response.get();
    }
}
