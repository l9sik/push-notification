package com.poluectov.authorizationserver.authentication.provider;

import com.poluectov.authorizationserver.authentication.token.JwtAuthenticationToken;
import com.poluectov.authorizationserver.model.UserInfoDTO;
import com.poluectov.authorizationserver.model.dto.AuthenticationRequestDto;
import com.poluectov.authorizationserver.model.dto.AuthenticationResponseDto;
import com.poluectov.authorizationserver.model.oauth2.AuthenticationRegistrationId;
import com.poluectov.authorizationserver.repository.AuthorizationRepository;
import com.poluectov.authorizationserver.service.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Optional;

@AllArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    JwtService service;
    AuthorizationRepository authorizationRepository;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;

        String jwt = (String) jwtAuthenticationToken.getCredentials();

        String userName = service.extractUserName(jwt);

        AuthenticationRequestDto authenticationRequestDto = AuthenticationRequestDto.builder()
                .username(userName)
                .userEmail(service.extractEmail(jwt))
                .build();

        Optional<AuthenticationResponseDto> responseDto = authorizationRepository.find(authenticationRequestDto);

        if (responseDto.isEmpty()){
            throw new AuthenticationCredentialsNotFoundException("Jwt token is not valid");
        }

        if (service.isTokenValid(jwt, mapToUserInfoDto(responseDto.get()))) {
            Collection<? extends GrantedAuthority> grantedAuthorities = responseDto.get().getAuthorities().stream().map((a) ->
                    new SimpleGrantedAuthority(a.getAuthority())).toList();

            //return new JwtAuthenticationToken(jwt, service);
            return new UsernamePasswordAuthenticationToken(userName, null, grantedAuthorities);
        }
        throw new AuthenticationCredentialsNotFoundException("Jwt token is not valid");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private UserInfoDTO mapToUserInfoDto(AuthenticationResponseDto responseDto){

        return UserInfoDTO.builder()
                .name(responseDto.getUserName())
                .email(responseDto.getUserEmail())
                .build();
    }
}
