package com.example.consumer.consumer;

import com.example.core.event.NotificationEvent;
import com.example.consumer.service.NotificationProcessorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventConsumer {

    private final NotificationProcessorService notificationProcessorService;

    @KafkaListener(topics = "notification-topic", groupId = "notification-consumer-group")
    public void consume(NotificationEvent event) {
        log.info("Received notification event: {}", event);

        notificationProcessorService.process(event);
    }
}