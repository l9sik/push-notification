package com.poluectov.notificationservice.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @Bean
    FirebaseOptions firebaseOptions() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(
                new ClassPathResource("push-notification-96faa-firebase-adminsdk-vgp3r-88d4e655f7.json")
                        .getInputStream());
        return FirebaseOptions.builder()
                .setCredentials(googleCredentials)
                .build();
    }

    @Bean
    FirebaseMessaging firebaseMessaging(FirebaseOptions firebaseOptions) {
        FirebaseApp app = FirebaseApp.initializeApp(firebaseOptions, "notification_service");
        return FirebaseMessaging.getInstance(app);
    }
}
