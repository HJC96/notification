package com.example.consumer.consumer;

import com.example.consumer.factory.NotificationSenderFactory;
import com.example.consumer.sender.NotificationSender;
import com.example.core.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationEventConsumer {

    private final NotificationSenderFactory senderFactory;

    @KafkaListener(topics = "notification-events", groupId = "notification-group")
    public void consume(NotificationEvent event) {
        NotificationSender sender = senderFactory.getSender(event.getNotificationType());
        sender.send(event);
    }
}