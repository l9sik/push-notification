package com.poluectov.authorizationserver.config;

import com.poluectov.authorizationserver.authentication.JwtLogoutHandler;
import com.poluectov.authorizationserver.authentication.filter.JWTAuthenticationFilter;
import com.poluectov.authorizationserver.authentication.filter.JWTVerifierFilter;
import com.poluectov.authorizationserver.authentication.provider.JwtAuthenticationProvider;
import com.poluectov.authorizationserver.authentication.provider.UsernameEmailPasswordAuthenticationProvider;
import com.poluectov.authorizationserver.repository.AuthorizationRepository;
import com.poluectov.authorizationserver.service.JwtService;
import com.poluectov.authorizationserver.service.TokenRegistrationService;
import com.poluectov.authorizationserver.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {
    ObjectMapper objectMapper;
    JwtService jwtService;
    AuthenticationManagerBuilder authenticationManagerBuilder;
    AuthorizationRepository authorizationRepository;
    JwtLogoutHandler jwtLogoutHandler;
    PasswordEncoder passwordEncoder;

    TokenService tokenService;
    TokenRegistrationService tokenRegistrationService;

    @Autowired
    @Qualifier("delegatedAuthenticationEntryPoint")
    AuthenticationEntryPoint authEntryPoint;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        authenticationManagerBuilder
                .authenticationProvider(new UsernameEmailPasswordAuthenticationProvider(authorizationRepository, passwordEncoder))
                .authenticationProvider(new JwtAuthenticationProvider(jwtService, authorizationRepository));
        return http
                .exceptionHandling(exceptionHandler -> exceptionHandler
                        .authenticationEntryPoint(authEntryPoint)
                )
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        authz -> authz
                                .requestMatchers("/auth/register", "/auth/token").permitAll()
                                .requestMatchers("/login/**").permitAll()
                                .requestMatchers("/auth/forgot/password").permitAll()
                                .anyRequest().authenticated()
                )
                .addFilter(new JWTAuthenticationFilter(
                        objectMapper, authenticationManagerBuilder.getOrBuild(), tokenRegistrationService))
                .addFilterAfter(
                        new JWTVerifierFilter(jwtService, authorizationRepository, authenticationManagerBuilder.getOrBuild()),
                        JWTAuthenticationFilter.class)
                .logout(AbstractHttpConfigurer::disable)
                .build();
    }

}
