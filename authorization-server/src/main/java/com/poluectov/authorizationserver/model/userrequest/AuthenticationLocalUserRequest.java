package com.poluectov.authorizationserver.model.userrequest;

import com.poluectov.authorizationserver.model.oauth2.AuthenticationRegistrationId;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Builder
@Getter
@Setter
public class AuthenticationLocalUserRequest {

    private String username;

    private String userEmail;

    private String password;
}
