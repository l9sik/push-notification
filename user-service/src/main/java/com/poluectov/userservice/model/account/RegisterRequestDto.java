package com.poluectov.userservice.model.account;

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