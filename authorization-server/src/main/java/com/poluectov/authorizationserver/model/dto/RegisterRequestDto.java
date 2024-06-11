package com.poluectov.authorizationserver.model.dto;

import com.poluectov.authorizationserver.model.oauth2.AuthenticationRegistrationId;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class RegisterRequestDto {


    String userEmail;

    String username;

    String userPassword;

}
