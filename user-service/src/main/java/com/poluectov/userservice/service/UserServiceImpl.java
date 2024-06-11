package com.poluectov.userservice.service;

import com.poluectov.userservice.entity.Authority;
import com.poluectov.userservice.model.account.AuthenticationResponseDto;
import com.poluectov.userservice.model.account.AuthorityResponseDto;
import com.poluectov.userservice.model.account.RegisterRequestDto;
import com.poluectov.userservice.entity.User;
import com.poluectov.userservice.model.user.UserResponseDto;
import com.poluectov.userservice.repository.AuthorityRepository;
import com.poluectov.userservice.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    @Override
    @Transactional
    public void create(RegisterRequestDto userRequestDto){
        //saving user data in data base
        User user = mapToUser(userRequestDto);
        Authority userAuthority = authorityRepository.findByAuthority("ROLE_USER");
        if (userAuthority != null) {
            user.getAuthorities().add(userAuthority);
        }
        userRepository.save(user);
    }

    @Override
    public List<UserResponseDto> all(){
        List<User> users = userRepository.findAll();

        return users.stream().map(this::mapToResponseDto).toList();
    }

    @Override
    public List<UserResponseDto> allByUserIds(List<Long> userIds) {
        List<User> users = userRepository.findAllById(userIds);

        return users.stream().map(this::mapToResponseDto).toList();
    }

    @Override
    public Optional<UserResponseDto> one(Long id){

        Optional<User> user = userRepository.findById(id);

        return user.flatMap(
                (u) -> Optional.ofNullable(mapToResponseDto(u))
        );
    }

    @Override
    public Optional<UserResponseDto> findByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User with email "
                        + email + " not found")
        );

        return Optional.ofNullable(mapToResponseDto(user));
    }

    @Override
    public AuthenticationResponseDto authenticateViaEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User with email "
                                + email + " not found")
        );

        return mapToAuthenticationResponseDto(user);
    }

    @Override
    public AuthenticationResponseDto authenticateViaUsername(String username) {
        User user = userRepository.findByLogin(username).orElseThrow(
                () -> new EntityNotFoundException("User with username "
                                + username + " not found")
        );

        return mapToAuthenticationResponseDto(user);
    }

    private AuthenticationResponseDto mapToAuthenticationResponseDto(User user){

        return AuthenticationResponseDto.builder()
                .userId(user.getId())
                .userName(user.getLogin())
                .userEmail(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getAuthorities().stream()
                        .map(this::mapToAuthorityResponseDto)
                        .toList()
                )
                .build();
    }

    private AuthorityResponseDto mapToAuthorityResponseDto(Authority authority){

        return AuthorityResponseDto.builder()
                .authority(authority.getAuthority())
                .build();
    }

    private User mapToUser(RegisterRequestDto userRequestDto){
        return User.builder()
                .login(userRequestDto.getUsername())
                .email(userRequestDto.getUserEmail())
                .password(userRequestDto.getUserPassword())
                .authorities(new HashSet<>())
                .build();
    }

    private UserResponseDto mapToResponseDto(User user){
        if (user == null){
            return null;
        }

        return UserResponseDto.builder()
                .id(user.getId())
                .login(user.getLogin())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }


}
