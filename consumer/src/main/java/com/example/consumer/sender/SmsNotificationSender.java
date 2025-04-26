package com.example.consumer.sender;

import com.example.core.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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

    public class SmsService {
        public void sendSms(String phoneNumber, String message) {
            // 실제 SMS API 호출 로직 작성

            // 예시) 외부 API 호출 (RestTemplate 사용)
            // restTemplate.postForEntity(smsApiUrl, 요청객체, 응답객체.class);

            System.out.println(" 가상 SMS 전송: " + phoneNumber + " / " + message);

            // 실제로는 HTTP 요청 보내야 하고,
            // 실패 시 예외 던지는 로직도 들어가야 해요.
        }
    }
}