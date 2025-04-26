package com.example.consumer.sender;

import com.example.consumer.service.SmsService;
import com.example.core.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Component
@RequiredArgsConstructor
public class SmsNotificationSender implements NotificationSender {
    private final SmsService smsService; // 실제 SMS 발송을 담당할 서비스 (외부 연동)

    @Override
    public void send(NotificationEvent event) {
        try {
            smsService.sendSms(event.getRecipientPhone(), event.getContent());

            log.info("SMS 발송 완료: to={}, message={}", event.getRecipientPhone(), event.getContent());
        } catch (Exception e) {
            log.error("SMS 발송 실패: to={}, error={}", event.getRecipientPhone(), e.getMessage(), e);
            throw new RuntimeException("SMS 발송 실패", e);
        }
    }
}