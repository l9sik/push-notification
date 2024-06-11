package com.poluectov.authorizationserver.model.dto;

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