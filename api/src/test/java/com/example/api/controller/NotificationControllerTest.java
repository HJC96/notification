package com.example.api.controller;

import com.example.api.ApiApplicationTest;
import com.example.api.dto.request.EmailNotificationRequest;
import com.example.api.dto.request.PushNotificationRequest;
import com.example.api.dto.request.SmsNotificationRequest;
import com.example.api.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
@ContextConfiguration(classes = ApiApplicationTest.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private NotificationService notificationService;

    @BeforeEach
    void setup() {
        // Service 메소드 호출시 아무것도 안 하게 함
        doNothing().when(notificationService).sendEmailNotification(any());
        doNothing().when(notificationService).sendPushNotification(any());
        doNothing().when(notificationService).sendSmsNotification(any());
    }

    @Test
    void 이메일_알림_API를_호출하면_201을_반환한다() throws Exception {
        EmailNotificationRequest request = new EmailNotificationRequest(
                1L,
                "제목",
                Map.of("body", "내용"),
                "email-template:default"
        );

        mockMvc.perform(post("/api/notification/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(notificationService).sendEmailNotification(Mockito.any(EmailNotificationRequest.class));
    }

    @Test
    void 푸시_알림_API를_호출하면_201을_반환한다() throws Exception {
        PushNotificationRequest request = new PushNotificationRequest(1L, "푸시 제목", "푸시 내용");

        mockMvc.perform(post("/api/notification/push")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(notificationService).sendPushNotification(Mockito.any(PushNotificationRequest.class));
    }

    @Test
    void SMS_알림_API를_호출하면_201을_반환한다() throws Exception {
        SmsNotificationRequest request = new SmsNotificationRequest(1L, "SMS 메세지");

        mockMvc.perform(post("/api/notification/sms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(notificationService).sendSmsNotification(Mockito.any(SmsNotificationRequest.class));
    }
}
