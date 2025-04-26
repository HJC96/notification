package com.example.api.controller;

import com.example.api.ApiApplicationTest;
import com.example.api.dto.NotificationRequest;
import com.example.api.service.NotificationService;
import com.example.core.domain.UserNotificationMetadata;
import com.example.core.event.NotificationEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * NotificationController 테스트
 */
@WebMvcTest(NotificationController.class)
@ContextConfiguration(classes = ApiApplicationTest.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired // TestBeansConfig에서 mock 주입한것을 가져옴
    private NotificationService notificationService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        // Service 메소드 호출시 아무것도 안 하게 함
        doNothing().when(notificationService).sendNotification(any());
    }

    @Test
    @DisplayName("POST /notifications - 알림 생성 요청이 정상적으로 처리된다")
    void sendNotification_success() throws Exception {
        NotificationRequest request = new NotificationRequest(1L, "Test Title", "Test Body");

        mockMvc.perform(post("/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(notificationService).sendNotification(Mockito.refEq(request));
    }
}