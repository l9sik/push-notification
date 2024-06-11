package com.poluectov.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RestError {
    HttpStatus status;
    String message;
}
