package com.poluectov.subscriptionservice.filter;

import com.poluectov.subscriptionservice.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@Component
@AllArgsConstructor
@Order(HIGHEST_PRECEDENCE + 1)
public class AuthorizationFilter extends OncePerRequestFilter {

    JwtUtils jwtUtils;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (StringUtils.isEmpty(request.getHeader(HttpHeaders.AUTHORIZATION))) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        authHeader = authHeader.substring(7);

        String email = jwtUtils.extractEmail(authHeader);

        request.setAttribute("email", email);
        request.setAttribute("authorities", jwtUtils.extractAuthorities(authHeader));
        filterChain.doFilter(request, response);
    }
}