package com.example.consumer.consumer;

import com.example.consumer.factory.NotificationSenderFactory;
import com.example.consumer.sender.NotificationSender;
import com.example.core.event.NotificationEvent;
import com.example.core.event.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationEventConsumer {

    private final NotificationSenderFactory senderFactory;
    private final AtomicBoolean handled = new AtomicBoolean(false);

    @RetryableTopic(
            attempts = "4", // 최초 + 3번 재시도
            backoff = @Backoff(delay = 2000), // 2초마다 재시도
            dltTopicSuffix = "-dlq" // 실패 시 notification-event-dlq 로 이동
    )
    @KafkaListener(topics = "notification-event", groupId = "notification-group")
    public void consume(@Payload NotificationEvent event) {
        NotificationSender sender = senderFactory.getSender(event.getNotificationType());

        if (event.getNotificationType() == NotificationType.PUSH) {
            // 강제 실패해서 Retry 테스트
            log.warn("Push Notification 실패, Retry 예정");
            throw new RuntimeException("Push notification error");
        }

        sender.send(event);
        log.info("Notification 처리 완료: {}", event);
    }

    @DltHandler
    public void dltConsume(@Payload NotificationEvent event) {
        log.error("DLQ 수신: {}", event);
    }

    public boolean isHandled() {
        return handled.get();
    }

    public void resetHandled() {
        handled.set(false);
    }
}