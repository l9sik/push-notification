package com.poluectov.authorizationserver.authentication;

import com.poluectov.authorizationserver.service.TokenService;
import com.poluectov.authorizationserver.util.AuthenticationUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Transactional
public class JwtLogoutHandler implements LogoutHandler {
    TokenService tokenService;
    AuthenticationUtils authenticationUtils;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String username = authenticationUtils.extractUsername(authentication);

        tokenService.deleteAllByUsername(username);
    }
}
