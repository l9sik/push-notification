package com.poluectov.authorizationserver.model.dto;

import com.poluectov.authorizationserver.model.entity.Authority;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Builder
@Getter
@Setter
public class AuthenticationResponseDto {

    private String userName;

    private String userEmail;

    private String password;

    Collection<Authority> authorities;

}
