package com.poluectov.authorizationserver.controller;

import com.poluectov.authorizationserver.authentication.JwtLogoutHandler;
import com.poluectov.authorizationserver.exception.UserNotFoundException;
import com.poluectov.authorizationserver.model.ConnValidationResponse;
import com.poluectov.authorizationserver.model.dto.*;
import com.poluectov.authorizationserver.model.userrequest.AuthenticationLocalUserRequest;
import com.poluectov.authorizationserver.model.userrequest.RegisterLocalUserRequest;
import com.poluectov.authorizationserver.repository.AuthorizationRepository;
import com.poluectov.authorizationserver.service.AuthenticationRegistrationService;
import com.poluectov.authorizationserver.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class IdentityProviderController {

    AuthorizationRepository authorizationRepository;

    AuthenticationRegistrationService authenticationRegistrationService;

    JwtService jwtService;
    JwtLogoutHandler logoutHandler;


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponseDto register(@RequestBody RegisterLocalUserRequest registerLocalUserRequest,
                                        HttpServletResponse response) {
        RegisterResponseDto registerResponseDto = authenticationRegistrationService.register(registerLocalUserRequest);
        response.setStatus(registerResponseDto.getHttpStatus().value());
        return registerResponseDto;
    }

    @PostMapping("/token")
    @ResponseStatus(HttpStatus.OK)
    public ConnValidationResponse token(@RequestBody AuthenticationLocalUserRequest authDto, HttpServletResponse response)
            throws UserNotFoundException {
        ConnValidationResponse connValidationResponse = authenticationRegistrationService.authenticate(authDto);
        response.setStatus(connValidationResponse.getStatus().value());
        return connValidationResponse;
    }

    @GetMapping(value = "/valid")
    @ResponseStatus(HttpStatus.OK)
    public ConnValidationResponse valid(HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        String jwt = (String) request.getAttribute("jwt");
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) request.getAttribute("authorities");

        return ConnValidationResponse.builder()
                .status(HttpStatus.OK)
                .isAuthenticated(true)
                .methodType("Bearer")
                .email(email)
                .token(jwt)
                .authorities(authorities.stream().map(GrantedAuthority::getAuthority).toList())
                .build();
    }

    @PostMapping(value = "/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        logoutHandler.logout(request, response, authentication);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
