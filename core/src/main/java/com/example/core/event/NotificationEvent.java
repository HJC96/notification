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

    private Long userId;         // 수신자
    private String title;        // 알림 제목
    private String body;         // 알림 본문
    private String targetToken;  // 알림을 보낼 단말기 토큰
    private NotificationType type; // 알림 타입 (PUSH, SMS, EMAIL 구분용)


}