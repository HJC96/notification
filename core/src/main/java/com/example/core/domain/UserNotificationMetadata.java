package com.example.core.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_notification_metadata")
@Getter
@Setter
public class UserNotificationMetadata {

    @Id
    private Long userId; // 사용자 고유 ID

    private String deviceToken; // 단말기 토큰
    private String platform;    // iOS / Android

    private String phoneNumber; // SMS 발송용 전화번호
    private String email;       // 이메일 발송용 이메일 주소

    private boolean notificationEnabled; // 전체 알림 수신 동의 여부
    private boolean pushEnabled;          // 푸시 알림 수신 여부
    private boolean smsEnabled;           // SMS 수신 여부
    private boolean emailEnabled;         // 이메일 수신 여부

    private LocalDateTime lastUpdatedAt;  // 마지막 업데이트 시간
}