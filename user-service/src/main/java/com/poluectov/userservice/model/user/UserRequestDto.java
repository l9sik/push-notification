package com.poluectov.userservice.model.user;

import lombok.Data;

@Data
public class UserRequestDto {

    String login;
    String password;
}
