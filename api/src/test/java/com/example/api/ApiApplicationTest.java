package com.example.api;

import com.example.api.service.NotificationService;
import com.example.core.domain.UserNotificationMetadata;
import com.example.core.event.NotificationEvent;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;

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