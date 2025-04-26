package com.example.consumer.consumer;

import com.example.core.event.NotificationEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Component
public class NotificationEventConsumer {

    private final CountDownLatch latch = new CountDownLatch(1);
    private volatile boolean handled = false;

    @KafkaListener(topics = "notification-event", groupId = "notification-consumer-group")
    public void consume(NotificationEvent event) {
        System.out.println("Consumed event: " + event);
        handled = true;
        latch.countDown(); // 메시지 소비하면 latch 감소
    }

    public boolean isHandled() {
        return handled;
    }

    public void reset() {
        handled = false;
    }

    public boolean awaitMessage(long timeoutSeconds) throws InterruptedException {
        return latch.await(timeoutSeconds, TimeUnit.SECONDS);
    }
}