package com.example.api.service;

import com.example.api.dto.NotificationRequest;
import com.example.core.domain.UserNotificationMetadata;
import com.example.core.event.NotificationEvent;
import com.example.core.repository.UserNotificationMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class NotificationService {

    private final UserNotificationMetadataRepository metadataRepository;
    private final RedisTemplate<String, UserNotificationMetadata> redisTemplate;
    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    private static final String TOPIC = "notification-event";
    private static final String USER_META_KEY_PREFIX = "user-meta:";

    public NotificationService(UserNotificationMetadataRepository metadataRepository, @Qualifier("userNotificationMetadataRedisTemplate") RedisTemplate<String, UserNotificationMetadata> redisTemplate, KafkaTemplate<String, NotificationEvent> kafkaTemplate) {
        this.metadataRepository = metadataRepository;
        this.redisTemplate = redisTemplate;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendNotification(NotificationRequest request) {
        String key = USER_META_KEY_PREFIX + request.getUserId();

        // 1. Redis 조회
        UserNotificationMetadata metadata = redisTemplate.opsForValue().get(key);

        // 2. 캐시에 없으면 DB 조회
        if (metadata == null) {
            metadata = metadataRepository.findById(request.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("사용자 메타데이터를 찾을 수 없습니다."));

            // 3. 조회된 결과를 Redis에 캐시 (TTL은 30분으로 예시)
            redisTemplate.opsForValue().set(key, metadata, 30, TimeUnit.MINUTES);
        }

        // 4. Kafka 이벤트 생성 및 발행
        NotificationEvent event = NotificationEvent.builder()
                .userId(request.getUserId())
                .title(request.getTitle())
                .body(request.getBody())
                .targetToken(metadata.getDeviceToken())
                .type(NotificationEvent.NotificationType.PUSH)
                .build();

        kafkaTemplate.send(TOPIC, event);
    }
}