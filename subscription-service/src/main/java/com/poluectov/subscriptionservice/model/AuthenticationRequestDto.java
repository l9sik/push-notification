package com.poluectov.subscriptionservice.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AuthenticationRequestDto {

    private String username;
    private String userEmail;

}