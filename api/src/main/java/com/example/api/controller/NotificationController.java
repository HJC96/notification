package com.example.api.controller;

import com.example.api.dto.request.EmailNotificationRequest;
import com.example.api.dto.request.PushNotificationRequest;
import com.example.api.dto.request.SmsNotificationRequest;
import com.example.api.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/email")
    @Operation(summary = "이메일 알림 전송", description = "특정 사용자에게 이메일 알림을 전송합니다.")
    public ResponseEntity<Void> sendEmail(@RequestBody EmailNotificationRequest request) {
        notificationService.sendEmailNotification(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/push")
    @Operation(summary = "푸시 알림 전송", description = "특정 사용자에게 푸시 알림을 전송합니다.")
    public ResponseEntity<Void> sendPush(@RequestBody PushNotificationRequest request) {
        notificationService.sendPushNotification(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/sms")
    @Operation(summary = "SMS 알림 전송", description = "특정 사용자에게 SMS 알림을 전송합니다.")
    public ResponseEntity<Void> sendSms(@RequestBody SmsNotificationRequest request) {
        notificationService.sendSmsNotification(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}