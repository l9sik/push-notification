package com.poluectov.authorizationserver.model;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.Collection;

@Getter
@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ConnValidationResponse {
    private HttpStatus status;
    private boolean isAuthenticated;
    private String methodType;
    private String email;
    private Collection<String> authorities;
    private String token;
}