package com.poluectov.subscriptionservice.repository;

import com.poluectov.subscriptionservice.model.AuthenticationResponseDto;
import org.springframework.stereotype.Service;

import java.util.Optional;


public interface AuthorizationRepository {


    Optional<AuthenticationResponseDto> find(String email);
}