package com.poluectov.userservice.model.user;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserResponseDto {

    Long id;

    String login;
    String email;
    String password;
}
