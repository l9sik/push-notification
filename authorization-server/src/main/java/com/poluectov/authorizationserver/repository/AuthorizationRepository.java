package com.poluectov.authorizationserver.repository;

import com.poluectov.authorizationserver.model.dto.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface AuthorizationRepository {


    Optional<AuthenticationResponseDto> find(AuthenticationRequestDto authenticationRequestDto);
    RegisterResponseDto save(RegisterRequestDto newUser);
}
