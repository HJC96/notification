package com.example.api;

import com.example.api.service.NotificationService;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "com.example.api") // 이걸 가져와야 webMvc 사용할 수 있다. 이거 없으면 엔티티매니지 오류뜸
public class ApiApplicationTest {
    @TestConfiguration
    static class TestBeansConfig {
        @Bean
        public NotificationService notificationService() {
            return Mockito.mock(NotificationService.class);
        }
    }
}