package com.poluectov.userservice.controller;

import com.poluectov.userservice.exception.BadRequestException;
import com.poluectov.userservice.model.account.AuthenticationRequestDto;
import com.poluectov.userservice.model.account.AuthenticationResponseDto;
import com.poluectov.userservice.model.account.RegisterRequestDto;
import com.poluectov.userservice.model.account.RegisterResponseDto;
import com.poluectov.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final UserService userService;

    @PostMapping("/reg")
    RegisterResponseDto register(@RequestBody RegisterRequestDto requestDto) {
        try {
            userService.create(requestDto);
        }catch (Exception e) {
            return RegisterResponseDto.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message(e.getMessage()).build();
        }
        return RegisterResponseDto.builder()
                .httpStatus(HttpStatus.OK)
                .message("Success").build();
    }

    @PostMapping("/auth")
    AuthenticationResponseDto authenticate(@RequestBody AuthenticationRequestDto requestDto) {
        String email = requestDto.getUserEmail();
        if (email != null) {
            return userService.authenticateViaEmail(email);
        }

        String name = requestDto.getUsername();
        if (name != null) {
            return userService.authenticateViaUsername(name);
        }

        throw new BadRequestException("Request body must contain either username or email");
    }

}
