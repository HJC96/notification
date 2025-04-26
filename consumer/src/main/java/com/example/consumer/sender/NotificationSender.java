package com.example.consumer.sender;

import com.example.core.event.NotificationEvent;

public interface NotificationSender {
    void send(NotificationEvent event);
}
