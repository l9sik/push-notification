package com.poluectov.userservice.model.account;

import lombok.*;
import org.springframework.http.HttpStatus;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterResponseDto {

    HttpStatus httpStatus;
    String message;

}
