package com.example.consumer.sender;

import com.example.core.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PushNotificationSender implements NotificationSender{

    private final PushService pushService;

    @Override
    public void send(NotificationEvent event) {
        try {
            pushService.sendPush(event.getDeviceToken(), event.getTitle(), event.getContent());

            log.info("Push 발송 완료: deviceToken={}, title={}, content={}",
                    event.getDeviceToken(), event.getTitle(), event.getContent());
        } catch (Exception e) {
            log.error("Push 발송 실패: deviceToken={}, error={}", event.getDeviceToken(), e.getMessage(), e);
            throw new RuntimeException("Push 발송 실패", e);
        }
    }

    class PushService {

        public void sendPush(String deviceToken, String title, String body) {
            System.out.println("📱 가상 Push 전송: deviceToken=" + deviceToken +
                    " / title=" + title + " / body=" + body);
        }
    }
}
