package com.example.api.service.impl;

import com.example.api.dto.request.EmailNotificationRequest;
import com.example.api.dto.request.PushNotificationRequest;
import com.example.api.dto.request.SmsNotificationRequest;
import com.example.api.service.NotificationService;
import com.example.core.domain.UserNotificationMetadata;
import com.example.core.event.NotificationEvent;
import com.example.core.event.NotificationType;
import com.example.core.repository.UserNotificationMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;

import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {

    private static final String USER_META_KEY_PREFIX = "user-meta:";
    private static final String COMMON_EMAIL_TEMPLATE_KEY = "email-template:default";
    private static final String TOPIC = "notification-event";

    private final UserNotificationMetadataRepository metadataRepository;
    @Qualifier("userNotificationMetadataRedisTemplate")
    private final RedisTemplate<String, UserNotificationMetadata> userMetadataRedisTemplate;
    @Qualifier("stringRedisTemplate")
    private final RedisTemplate<String, String> stringRedisTemplate;
    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    @Override
    public void sendEmailNotification(EmailNotificationRequest request) {
        UserNotificationMetadata metadata = loadUserMetadata(request.getUserId());
        String emailTemplate = loadEmailTemplate();

        NotificationEvent event = NotificationEvent.builder()
                .notificationType(NotificationType.EMAIL)
                .title(request.getSubject())
                .content(emailTemplate.replace("{{body}}", request.getBody()))
                .recipientEmail(metadata.getEmail())
                .build();

        kafkaTemplate.send(TOPIC, event);
    }

    @Override
    public void sendPushNotification(PushNotificationRequest request) {
        UserNotificationMetadata metadata = loadUserMetadata(request.getUserId());

        NotificationEvent event = NotificationEvent.builder()
                .notificationType(NotificationType.PUSH)
                .title(request.getTitle())
                .content(request.getBody())
                .deviceToken(metadata.getDeviceToken())
                .build();

        kafkaTemplate.send(TOPIC, event);
    }

    @Override
    public void sendSmsNotification(SmsNotificationRequest request) {
        UserNotificationMetadata metadata = loadUserMetadata(request.getUserId());

        NotificationEvent event = NotificationEvent.builder()
                .notificationType(NotificationType.SMS)
                .title("[SMS]")
                .content(request.getMessage())
                .recipientPhone(metadata.getPhoneNumber())
                .build();

        kafkaTemplate.send(TOPIC, event);
    }

    private UserNotificationMetadata loadUserMetadata(Long userId) {
        String key = USER_META_KEY_PREFIX + userId;
        UserNotificationMetadata metadata = userMetadataRedisTemplate.opsForValue().get(key);

        if (metadata == null) {
            metadata = metadataRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("사용자 메타데이터를 찾을 수 없습니다."));
            userMetadataRedisTemplate.opsForValue().set(key, metadata, 30, TimeUnit.MINUTES);
        }

        return metadata;
    }

    private String loadEmailTemplate() {
        String template = stringRedisTemplate.opsForValue().get(COMMON_EMAIL_TEMPLATE_KEY);
        if (template == null) {
            template = "<html><body>{{body}}</body></html>";
            stringRedisTemplate.opsForValue().set(COMMON_EMAIL_TEMPLATE_KEY, template, 30, TimeUnit.MINUTES);
        }
        return template;
    }
}