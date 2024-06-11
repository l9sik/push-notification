package com.poluectov.authorizationserver.authentication.token;

import com.poluectov.authorizationserver.service.JwtService;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

@Getter

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final String jwt;

    private final String username;
    private final String email;

    public JwtAuthenticationToken(String jwt, JwtService jwtService) throws RuntimeException {
        super(jwtService.extractAuthorities(jwt));
        this.jwt = jwt;
        this.username = jwtService.extractUserName(jwt);
        this.email = jwtService.extractEmail(jwt);
    }
    @Override
    public Object getCredentials() {
        return jwt;
    }

    @Override
    public Object getPrincipal() {
        return jwt;
    }
}
