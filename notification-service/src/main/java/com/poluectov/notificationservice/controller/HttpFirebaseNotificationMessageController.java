package com.poluectov.notificationservice.controller;

import com.poluectov.notificationservice.model.FirebaseNotificationMessage;
import com.poluectov.notificationservice.service.FirebaseNotificationMessageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class HttpFirebaseNotificationMessageController {
    private final FirebaseNotificationMessageServiceImpl notificationService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    void postNotification(@RequestBody FirebaseNotificationMessage message){
        notificationService.postNotification(message);
    }
}
