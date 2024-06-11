package com.poluectov.authorizationserver.config;


import io.netty.resolver.DefaultAddressResolverGroup;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;


@Configuration
@AllArgsConstructor
public class AdditionalServiceConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebProperties.Resources resources() {
        return new WebProperties.Resources();
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    @Bean
    public HttpClient httpClient() {
        return HttpClient.create().resolver(DefaultAddressResolverGroup.INSTANCE);
    }

}
