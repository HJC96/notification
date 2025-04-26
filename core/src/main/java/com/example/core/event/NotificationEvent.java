package com.example.core.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEvent {

    private NotificationType notificationType; // 알림 종류 (SMS, EMAIL, PUSH)

    private String title;           // 알림 제목
    private String content;         // 알림 내용

    private String recipientEmail;  // 이메일 수신자
    private String recipientPhone;  // SMS 수신자
    private String deviceToken;     // 푸시 토큰
}