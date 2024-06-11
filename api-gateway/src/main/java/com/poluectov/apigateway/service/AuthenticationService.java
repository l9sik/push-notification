package com.poluectov.apigateway.service;

import com.poluectov.apigateway.exception.AuthenticationInternalServerErrorException;
import com.poluectov.apigateway.exception.AuthenticationInvalidCredentialsException;
import com.poluectov.apigateway.exception.AuthenticationRequestNotValidException;
import com.poluectov.apigateway.exception.AuthenticationResourceNotFoundException;
import com.poluectov.apigateway.model.AuthorizationDto;
import com.poluectov.apigateway.model.ConnValidationResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.sql.SQLOutput;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class AuthenticationService {

    WebClient.Builder webClientBuilder;

    public Mono<ConnValidationResponse> authenticate(AuthorizationDto authorizationDto) {

        return webClientBuilder.build()
                .post()
                .uri("lb://identity-server/auth/token")
                .body(authorizationDto, AuthorizationDto.class)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        errorHandler()
                )
                .bodyToMono(ConnValidationResponse.class);
    }

    public Mono<ConnValidationResponse> validate(String token) {
        return webClientBuilder.build()
                .get()
                .uri("lb://identity-server/auth/valid")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        errorHandler()
                )
                .bodyToMono(ConnValidationResponse.class);
    }

    private Function<ClientResponse, Mono<? extends Throwable>> errorHandler() {
        return response ->
                switch (response.statusCode().value()) {
            case 400 ->
                    Mono.error(new AuthenticationRequestNotValidException("The request was invalid."));
            case 401, 403 ->
                    Mono.error(new AuthenticationInvalidCredentialsException("The request did not include an authentication token or the authentication token was expired."));
            case 404 ->
                    Mono.error(new AuthenticationResourceNotFoundException("The requested resource was not found."));
            case 500 ->
                    Mono.error(new AuthenticationInternalServerErrorException("The request was not completed due to an internal error on the server side."));
            default ->
                    Mono.error(new AuthenticationInternalServerErrorException("Authentication failed"));
        };
    }
}
