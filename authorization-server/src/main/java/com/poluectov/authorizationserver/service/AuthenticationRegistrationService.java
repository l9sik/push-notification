package com.poluectov.authorizationserver.service;

import com.poluectov.authorizationserver.exception.EmailNotFoundException;
import com.poluectov.authorizationserver.exception.UserNotFoundException;
import com.poluectov.authorizationserver.model.ConnValidationResponse;
import com.poluectov.authorizationserver.model.RestError;
import com.poluectov.authorizationserver.model.UserInfoDTO;
import com.poluectov.authorizationserver.model.dto.*;
import com.poluectov.authorizationserver.model.entity.Authority;
import com.poluectov.authorizationserver.model.entity.MailType;
import com.poluectov.authorizationserver.model.oauth2.AuthenticationRegistrationId;
import com.poluectov.authorizationserver.model.userrequest.AuthenticationLocalUserRequest;
import com.poluectov.authorizationserver.model.userrequest.RegisterLocalUserRequest;
import com.poluectov.authorizationserver.repository.AuthorizationRepository;
import com.poluectov.authorizationserver.util.AuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class AuthenticationRegistrationService {

    JwtService jwtService;

    AuthorizationRepository authorizationRepository;

    PasswordEncoder passwordEncoder;

    TokenRegistrationService tokenRegistrationService;


    AuthenticationUtils authenticationUtils;

    LogoutHandler logoutHandler;


    @Autowired
    public AuthenticationRegistrationService(JwtService jwtService,
                                             AuthorizationRepository authorizationRepository,
                                             PasswordEncoder passwordEncoder,
                                             TokenRegistrationService tokenRegistrationService,
                                             AuthenticationUtils authenticationUtils,
                                             LogoutHandler logoutHandler) {
        this.jwtService = jwtService;
        this.authorizationRepository = authorizationRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRegistrationService = tokenRegistrationService;
        this.authenticationUtils = authenticationUtils;
        this.logoutHandler = logoutHandler;
    }

    public RegisterResponseDto register(RegisterLocalUserRequest registerLocalUserRequest) {
        registerLocalUserRequest.setPassword(passwordEncoder.encode(registerLocalUserRequest.getPassword()));
        return authorizationRepository.save(mapToRegisterDto(registerLocalUserRequest));
    }


    public ConnValidationResponse authenticate(AuthenticationLocalUserRequest authenticationRequestDto) throws UserNotFoundException {
        AuthenticationRequestDto authenticationRequest = AuthenticationRequestDto.builder()
                .username(authenticationRequestDto.getUsername())
                .userEmail(authenticationRequestDto.getUserEmail())
                .build();

        Optional<AuthenticationResponseDto> authentication = authorizationRepository.find(authenticationRequest);

        if (authentication.isEmpty()){
            throw new UserNotFoundException("Authentication failed");
        }

        if (!passwordEncoder.matches(authenticationRequestDto.getPassword(), authentication.get().getPassword())){

            throw new UserNotFoundException("Authentication failed");
        }

        String token = tokenRegistrationService.generateToken(mapToUserInfoDto(authentication.get()));

        return mapToConnValidationResponse(authentication.get(), token);
    }

    private RegisterRequestDto mapToRegisterDto(RegisterLocalUserRequest registerLocalUserRequest){
        return RegisterRequestDto.builder()
                .username(registerLocalUserRequest.getUsername())
                .userEmail(registerLocalUserRequest.getUserEmail())
                .userPassword(registerLocalUserRequest.getPassword())
                .build();
    }


    private ConnValidationResponse mapToConnValidationResponse(AuthenticationResponseDto authentication, String token) {
        return ConnValidationResponse.builder()
                .status(HttpStatus.OK)
                .isAuthenticated(true)
                .methodType("Bearer")
                .email(authentication.getUserEmail())
                .token(token)
                .authorities(authentication.getAuthorities().stream().map(Authority::getAuthority).toList())
                .build();
    }

    private UserInfoDTO mapToUserInfoDto(AuthenticationResponseDto authenticationResponseDto){
        return UserInfoDTO.builder()
                .name(authenticationResponseDto.getUserName())
                .email(authenticationResponseDto.getUserEmail())
                .authorities(authenticationResponseDto.getAuthorities().stream().map(Authority::getAuthority).collect(Collectors.toList()))
                .build();

    }
}
