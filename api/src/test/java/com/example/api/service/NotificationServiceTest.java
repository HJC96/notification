package com.example.api.service;

import com.example.api.dto.NotificationRequest;
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

class NotificationServiceTest {

    private NotificationService notificationService;
    private UserNotificationMetadataRepository metadataRepository;
    private RedisTemplate<String, UserNotificationMetadata> redisTemplate;
    private KafkaTemplate<String, NotificationEvent> kafkaTemplate;
    private ValueOperations<String, UserNotificationMetadata> valueOperations;

    @BeforeEach
    void setUp() {
        metadataRepository = mock(UserNotificationMetadataRepository.class);
        redisTemplate = mock(RedisTemplate.class);
        kafkaTemplate = mock(KafkaTemplate.class);
        valueOperations = mock(ValueOperations.class);

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        notificationService = new NotificationService(metadataRepository, redisTemplate, kafkaTemplate);
    }

    @Test
    void redis에_데이터가_있으면_DB조회없이_kafka발행() {
        // given
        NotificationRequest request = new NotificationRequest(1L, "Test Title", "Test Body");
        UserNotificationMetadata metadata = new UserNotificationMetadata();
        metadata.setDeviceToken("device-token");

        when(valueOperations.get("user-meta:1")).thenReturn(metadata);

        // when
        notificationService.sendNotification(request);

        // then
        // DB는 조회하지 않는다
        verify(metadataRepository, never()).findById(any());

        // Kafka로 이벤트를 발행한다
        verify(kafkaTemplate, times(1)).send(anyString(), any(NotificationEvent.class));
    }

    @Test
    void redis에_데이터가없으면_DB조회하고_kafka발행_redis캐시저장() {
        // given
        NotificationRequest request = new NotificationRequest(1L, "Test Title", "Test Body");

        when(valueOperations.get("user-meta:1")).thenReturn(null);

        UserNotificationMetadata metadata = new UserNotificationMetadata();
        metadata.setDeviceToken("device-token-from-db");
        when(metadataRepository.findById(1L)).thenReturn(Optional.of(metadata));

        // when
        notificationService.sendNotification(request);

        // then
        // DB를 조회한다
        verify(metadataRepository, times(1)).findById(1L);

        // 조회한 결과를 Redis에 캐시한다
        verify(valueOperations, times(1)).set(eq("user-meta:1"), eq(metadata), anyLong(), any());

        // Kafka로 이벤트를 발행한다
        verify(kafkaTemplate, times(1)).send(anyString(), any(NotificationEvent.class));
    }

    @Test
    void redis에도_DB에도없으면_예외발생() {
        // given
        NotificationRequest request = new NotificationRequest(1L, "Test Title", "Test Body");

        when(valueOperations.get("user-meta:1")).thenReturn(null);
        when(metadataRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        // 예외가 발생해야 한다
        assertThrows(IllegalArgumentException.class, () -> {
            notificationService.sendNotification(request);
        });

        // Kafka로는 아무것도 발행하지 않는다
        verify(kafkaTemplate, never()).send(any(), any());
    }
}