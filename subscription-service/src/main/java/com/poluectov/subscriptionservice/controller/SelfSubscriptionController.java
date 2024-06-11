package com.poluectov.subscriptionservice.controller;

import com.poluectov.subscriptionservice.model.SubscriptionRequestDto;
import com.poluectov.subscriptionservice.model.SubscriptionResponseDto;
import com.poluectov.subscriptionservice.service.SubscriptionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpRequest;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/subscriptions/self")
public class SelfSubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping
    public List<SubscriptionResponseDto> selfSubscription(HttpServletRequest request) {
        String email = (String) request.getAttribute("email");

        return subscriptionService.findByEmail(email);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SubscriptionResponseDto createSubscription(@RequestBody SubscriptionRequestDto subscriptionRequestDto, HttpServletRequest request, HttpServletResponse response) {
        String email = (String) request.getAttribute("email");

        return subscriptionService.create(subscriptionRequestDto, email);
    }


}
