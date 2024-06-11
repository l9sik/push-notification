package com.poluectov.authorizationserver.model.userrequest;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class RegisterLocalUserRequest {

    String username;
    String userEmail;
    String password;

}
