package com.poluectov.userservice.service;

import com.poluectov.userservice.model.account.AuthenticationResponseDto;
import com.poluectov.userservice.model.account.RegisterRequestDto;
import com.poluectov.userservice.model.account.RegisterResponseDto;
import com.poluectov.userservice.model.user.UserRequestDto;
import com.poluectov.userservice.model.user.UserResponseDto;

import java.util.List;
import java.util.Optional;

public interface UserService {

    void create(RegisterRequestDto userRequestDto);
    List<UserResponseDto> all();
    List<UserResponseDto> allByUserIds(List<Long> userIds);
    Optional<UserResponseDto> one(Long id);

    Optional<UserResponseDto> findByEmail(String email);

    AuthenticationResponseDto authenticateViaEmail(String email);
    AuthenticationResponseDto authenticateViaUsername(String username);


}
