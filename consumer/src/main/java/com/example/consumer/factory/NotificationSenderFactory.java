package com.example.consumer.factory;

import com.example.consumer.sender.EmailNotificationSender;
import com.example.consumer.sender.NotificationSender;
import com.example.consumer.sender.PushNotificationSender;
import com.example.consumer.sender.SmsNotificationSender;
import com.example.core.event.NotificationType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class NotificationSenderFactory {

    private final List<NotificationSender> senders;
    private Map<NotificationType, NotificationSender> senderMap;


    @PostConstruct
    public void init() {
        senderMap = new HashMap<>();
        for (NotificationSender sender : senders) {
            if (sender instanceof EmailNotificationSender) {
                senderMap.put(NotificationType.EMAIL, sender);
            } else if (sender instanceof SmsNotificationSender) {
                senderMap.put(NotificationType.SMS, sender);
            } else if (sender instanceof PushNotificationSender) {
                senderMap.put(NotificationType.PUSH, sender);
            }
        }
    }

    public NotificationSender getSender(NotificationType type) {
        return senderMap.get(type);
    }
}