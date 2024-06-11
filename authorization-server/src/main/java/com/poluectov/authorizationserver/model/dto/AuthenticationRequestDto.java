package com.poluectov.authorizationserver.model.dto;

import com.poluectov.authorizationserver.model.oauth2.AuthenticationRegistrationId;
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