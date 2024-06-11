package com.poluectov.testservice.controller;

import com.poluectov.testservice.model.UserNotificationRequest;
import com.poluectov.testservice.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {


    private final TestService testService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    void test(@RequestBody UserNotificationRequest request) {
        testService.sendNotification(request);
    }
}
