package com.example.api.service.impl;

import com.example.api.dto.request.EmailNotificationRequest;
import com.example.api.dto.request.PushNotificationRequest;
import com.example.api.dto.request.SmsNotificationRequest;
import com.example.core.domain.UserNotificationMetadata;
import com.example.core.event.NotificationEvent;
import com.example.core.repository.UserNotificationMetadataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class NotificationServiceImplTest {

    private NotificationServiceImpl notificationService;
    private UserNotificationMetadataRepository metadataRepository;
    private RedisTemplate<String, UserNotificationMetadata> userMetadataRedisTemplate;
    private RedisTemplate<String, String> stringRedisTemplate;
    private KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    private ValueOperations<String, UserNotificationMetadata> userValueOperations;
    private ValueOperations<String, String> stringValueOperations;

    @BeforeEach
    void setUp() {
        metadataRepository = mock(UserNotificationMetadataRepository.class);
        userMetadataRedisTemplate = mock(RedisTemplate.class);
        stringRedisTemplate = mock(RedisTemplate.class);
        kafkaTemplate = mock(KafkaTemplate.class);

        userValueOperations = mock(ValueOperations.class);
        stringValueOperations = mock(ValueOperations.class);

        when(userMetadataRedisTemplate.opsForValue()).thenReturn(userValueOperations);
        when(stringRedisTemplate.opsForValue()).thenReturn(stringValueOperations);

        notificationService = new NotificationServiceImpl(metadataRepository, userMetadataRedisTemplate, stringRedisTemplate, kafkaTemplate);
    }

    @Test
    void 푸시_알림_전송_성공() {
        // given
        PushNotificationRequest request = new PushNotificationRequest(1L, "Push Title", "Push Body");
        UserNotificationMetadata metadata = new UserNotificationMetadata();
        metadata.setDeviceToken("device-token");

        when(userValueOperations.get("user-meta:1")).thenReturn(metadata);

        // when
        notificationService.sendPushNotification(request);

        // then
        verify(kafkaTemplate, times(1)).send(anyString(), any(NotificationEvent.class));
    }

    @Test
    void 이메일_알림_전송_성공() {
        // given
        EmailNotificationRequest request = new EmailNotificationRequest(1L, "Subject", "Email Body");
        UserNotificationMetadata metadata = new UserNotificationMetadata();
        metadata.setEmail("test@example.com");

        when(userValueOperations.get("user-meta:1")).thenReturn(metadata);
        when(stringValueOperations.get("email-template:default")).thenReturn("<html><body>{{body}}</body></html>");

        // when
        notificationService.sendEmailNotification(request);

        // then
        verify(kafkaTemplate, times(1)).send(anyString(), any(NotificationEvent.class));
    }

    @Test
    void SMS_알림_전송_성공() {
        // given
        SmsNotificationRequest request = new SmsNotificationRequest(1L, "SMS Message");
        UserNotificationMetadata metadata = new UserNotificationMetadata();
        metadata.setPhoneNumber("010-1234-5678");

        when(userValueOperations.get("user-meta:1")).thenReturn(metadata);

        // when
        notificationService.sendSmsNotification(request);

        // then
        verify(kafkaTemplate, times(1)).send(anyString(), any(NotificationEvent.class));
    }

    @Test
    void 사용자_메타데이터_없으면_예외() {
        // given
        PushNotificationRequest request = new PushNotificationRequest(1L, "Title", "Body");

        when(userValueOperations.get("user-meta:1")).thenReturn(null);
        when(metadataRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(IllegalArgumentException.class, () -> notificationService.sendPushNotification(request));
    }
}
