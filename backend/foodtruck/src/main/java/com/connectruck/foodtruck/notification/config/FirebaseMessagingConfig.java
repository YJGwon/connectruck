package com.connectruck.foodtruck.notification.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@Slf4j
public class FirebaseMessagingConfig {

    @Bean
    FirebaseMessaging initialize() {
        final ClassPathResource firebaseAccountResource = new ClassPathResource("/config/firebase-admin-account.json");
        try {
            final FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(firebaseAccountResource.getInputStream()))
                    .build();

            FirebaseApp.initializeApp(options);
            log.info("Firebase app initialized");
            return FirebaseMessaging.getInstance();
        } catch (IOException e) {
            throw new RuntimeException("FirebaseApp을 초기화할 수 없습니다.", e);
        }
    }
}
