package com.example.consumer.consumer;

import com.example.consumer.factory.NotificationSenderFactory;
import com.example.consumer.sender.NotificationSender;
import com.example.core.event.NotificationEvent;
import com.example.core.event.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationEventConsumer {

    private final NotificationSenderFactory senderFactory;
    private final RedisTemplate<String, String> redisTemplate;
    private final AtomicInteger handledCount = new AtomicInteger(0);
    private static final int LIMIT = 5; // 초당 최대 5회


    @RetryableTopic(
            attempts = "4", // 최초 + 3번 재시도
            backoff = @Backoff(delay = 2000), // 2초마다 재시도
            dltTopicSuffix = "-dlq" // 실패 시 notification-event-dlq 로 이동
    )
    @KafkaListener(topics = "notification-event", groupId = "notification-group")
    public void consume(@Payload NotificationEvent event) {
        checkRateLimit(event.getUserId()); // 예시로 userId 기준 체크

        NotificationSender sender = senderFactory.getSender(event.getNotificationType());

        if (event.getNotificationType() == NotificationType.PUSH) {
            // 강제 실패해서 Retry 테스트
            log.warn("Push Notification 실패, Retry 예정");
            throw new RuntimeException("Push notification error");
        }

        sender.send(event);
        handledCount.incrementAndGet(); // 처리된 횟수 증가
        log.info("Notification 처리 완료: {}", event);
    }

    @DltHandler
    public void dltConsume(@Payload NotificationEvent event) {
        log.error("DLQ 수신: {}", event);
    }

    public int getHandledCount() {
        return handledCount.get();
    }

    public void resetHandled() {
        handledCount.set(0);
    }

    public boolean isHandled() {
        return handledCount.get()>0;
    }

    private void checkRateLimit(Long userId) {
        String key = "rate_limit:" + userId;
        Long count = redisTemplate.opsForValue().increment(key);
        if (count == 1) {
            redisTemplate.expire(key, Duration.ofSeconds(1)); // 처음이면 1초만 유지
        }
        if (count > LIMIT) {
            log.error("Rate limit 초과: userId = {}", userId);
            throw new RuntimeException("Too many requests");
        }
    }

}