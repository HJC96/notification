package com.example.api;

import com.example.api.service.NotificationService;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "com.example.api")
public class ApiApplicationTest {
    @TestConfiguration
    static class TestBeansConfig {
        @Bean
        public NotificationService notificationService() {
            return Mockito.mock(NotificationService.class);
        }
    }
}