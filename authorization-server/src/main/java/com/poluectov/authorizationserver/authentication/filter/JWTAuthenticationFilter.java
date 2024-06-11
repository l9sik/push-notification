package com.poluectov.authorizationserver.authentication.filter;

import com.poluectov.authorizationserver.authentication.token.UsernameEmailPasswordAuthenticationToken;
import com.poluectov.authorizationserver.model.ConnValidationResponse;
import com.poluectov.authorizationserver.model.UserInfoDTO;
import com.poluectov.authorizationserver.model.entity.MailType;
import com.poluectov.authorizationserver.model.userrequest.AuthenticationLocalUserRequest;
import com.poluectov.authorizationserver.service.TokenRegistrationService;
import com.poluectov.authorizationserver.util.SecurityConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.List;

import static java.util.stream.Collectors.toList;


@Slf4j
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;

    private final AuthenticationManager authenticationManager;

    TokenRegistrationService tokenRegistrationService;

    public JWTAuthenticationFilter(ObjectMapper objectMapper,
                                   AuthenticationManager authenticationManager,
                                   TokenRegistrationService tokenRegistrationService){
        setFilterProcessesUrl("/auth/token");
        this.objectMapper = objectMapper;
        this.authenticationManager = authenticationManager;
        this.tokenRegistrationService = tokenRegistrationService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            AuthenticationLocalUserRequest authDto = objectMapper.readValue(request.getInputStream(),
                    AuthenticationLocalUserRequest.class);
            return authenticationManager.authenticate(new UsernameEmailPasswordAuthenticationToken(authDto.getUsername(),
                    authDto.getUserEmail(),
                    authDto.getPassword()));
        } catch (IOException e) {
            log.error("Jwt Authentication failed in {}, {}", this.getClass(), e.getMessage());
            throw new AuthenticationCredentialsNotFoundException("Could not found user", e);
        }
    }


    // this is called when user is successfully authenticated in auth/token
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        UsernameEmailPasswordAuthenticationToken authToken = (UsernameEmailPasswordAuthenticationToken) authResult;

        //If user already have token then regenerate it and modify
        UserInfoDTO userInfo = mapToUserDetails(authToken);


        String token = tokenRegistrationService.generateToken(userInfo);


        //preparing response
        response.addHeader(SecurityConstants.HEADER, String.format("Bearer %s", token));


        ConnValidationResponse respModel = mapToConnValidationResponse(authToken, token);

        response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().write(objectMapper.writeValueAsBytes(respModel));
    }

    private UserInfoDTO mapToUserDetails(UsernameEmailPasswordAuthenticationToken authentication) {
        return UserInfoDTO.builder()
                .name(authentication.getUsername())
                .email(authentication.getUserEmail())
                .authorities(authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(toList()))
                .build();
    }

    private ConnValidationResponse mapToConnValidationResponse(UsernameEmailPasswordAuthenticationToken authentication,
                                                               String jwt) {

        List<String> authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();
        return ConnValidationResponse.builder()
                .status(HttpStatus.OK)
                .email(authentication.getUserEmail())
                .authorities(authorities)
                .token(jwt)
                .methodType(HttpMethod.GET.name())
                .isAuthenticated(true)
                .build();
    }
}
