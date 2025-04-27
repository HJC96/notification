package com.example.consumer.sender;

import com.example.core.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailNotificationSender implements NotificationSender{

    private final JavaMailSender mailSender;

    @Override
    public void send(NotificationEvent event) {
        try {
            /*SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(event.getRecipientEmail()); // 수신자 이메일
            message.setSubject(event.getTitle());     // 제목
            message.setText(event.getContent());      // 내용

            mailSender.send(message);*/

            log.info("Email 발송 완료: to={}, title={}, content={}", event.getRecipientEmail(), event.getTitle(), event.getContent());
        } catch (Exception e) {
            log.error("Email 발송 실패: to={}, error={}", event.getRecipientEmail(), e.getMessage(), e);
            throw new RuntimeException("Email 발송 실패", e);
        }
    }
}
