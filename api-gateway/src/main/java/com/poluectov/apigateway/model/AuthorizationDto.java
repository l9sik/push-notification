package com.poluectov.apigateway.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthorizationDto {

    private String email;
    private String password;

}
