package com.example.consumer.consumer;

import com.example.consumer.factory.NotificationSenderFactory;
import com.example.consumer.sender.NotificationSender;
import com.example.core.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
public class NotificationEventConsumer {

    private final NotificationSenderFactory senderFactory;
    private final AtomicBoolean handled = new AtomicBoolean(false);

    @KafkaListener(topics = "notification-event", groupId = "notification-group")
    public void consume(NotificationEvent event) {
        NotificationSender sender = senderFactory.getSender(event.getNotificationType());
        sender.send(event);
        handled.set(true); // 메시지를 소비했으면 handled를 true로 변경
    }

    public boolean isHandled() {
        return handled.get();
    }

    public void resetHandled() {
        handled.set(false);
    }
}