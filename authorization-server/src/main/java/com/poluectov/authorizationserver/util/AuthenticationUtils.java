package com.poluectov.authorizationserver.util;

import com.poluectov.authorizationserver.authentication.token.JwtAuthenticationToken;
import com.poluectov.authorizationserver.authentication.token.UsernameEmailPasswordAuthenticationToken;
import com.poluectov.authorizationserver.service.JwtService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationUtils {

    JwtService jwtService;

    public String extractUsername(Authentication authentication) {
        if (authentication instanceof UsernameEmailPasswordAuthenticationToken){
            return ((UsernameEmailPasswordAuthenticationToken) authentication).getUsername();
        }else if (authentication instanceof UsernamePasswordAuthenticationToken){
            return authentication.getName();
        }else if (authentication instanceof JwtAuthenticationToken){
            String token = (String) authentication.getPrincipal();
            return jwtService.extractUserName(token);
        }else {
            throw new UnsupportedOperationException("Unsupported authentication mechanism. Cannot extract username");
        }
    }

    public String extractEmail(Authentication authentication) {
        if (authentication instanceof UsernameEmailPasswordAuthenticationToken){
            return ((UsernameEmailPasswordAuthenticationToken) authentication).getUserEmail();
        }else if (authentication instanceof JwtAuthenticationToken){
            String token = (String) authentication.getPrincipal();
            return jwtService.extractEmail(token);
        }else {
            throw new UnsupportedOperationException("Unsupported authentication mechanism. Cannot extract email");
        }
    }
}
