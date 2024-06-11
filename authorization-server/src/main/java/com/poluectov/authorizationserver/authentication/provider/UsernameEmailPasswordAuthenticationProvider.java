package com.poluectov.authorizationserver.authentication.provider;


import com.poluectov.authorizationserver.authentication.token.UsernameEmailPasswordAuthenticationToken;
import com.poluectov.authorizationserver.exception.UserNotFoundException;
import com.poluectov.authorizationserver.model.dto.AuthenticationRequestDto;
import com.poluectov.authorizationserver.model.dto.AuthenticationResponseDto;
import com.poluectov.authorizationserver.model.oauth2.AuthenticationRegistrationId;
import com.poluectov.authorizationserver.repository.AuthorizationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
public class UsernameEmailPasswordAuthenticationProvider implements AuthenticationProvider {

    AuthorizationRepository authorizationRepository;
    PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernameEmailPasswordAuthenticationToken token = (UsernameEmailPasswordAuthenticationToken) authentication;

        AuthenticationRequestDto authenticationRequestDto = generateAuthenticationRequest(token);
        Optional<AuthenticationResponseDto> responseDto = authorizationRepository.find(authenticationRequestDto);

        if (responseDto.isEmpty()){
            log.error("User not found in {}", this.getClass());
            throw new AuthenticationCredentialsNotFoundException("User not found");
        }

        AuthenticationResponseDto response = responseDto.get();


        if (!passwordEncoder.matches(token.getPassword(), response.getPassword())){
            log.error("Invalid password in {}", this.getClass());
            throw new UserNotFoundException("Invalid password. Authentication failed");
        }

        return mapToUserEmailPasswordAuthenticationToken(responseDto.get());
    }

    private UsernameEmailPasswordAuthenticationToken mapToUserEmailPasswordAuthenticationToken(
            AuthenticationResponseDto response) {

        return new UsernameEmailPasswordAuthenticationToken(
                response.getUserName(),
                response.getUserEmail(),
                response.getPassword(),
                response.getAuthorities().stream().map((a) -> new SimpleGrantedAuthority(a.getAuthority()))
                        .collect(Collectors.toList())
        );
    }

    private AuthenticationRequestDto generateAuthenticationRequest(UsernameEmailPasswordAuthenticationToken token) {

        return AuthenticationRequestDto.builder()
                .userEmail(token.getUserEmail())
                .username(token.getUsername())
                .build();
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernameEmailPasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
