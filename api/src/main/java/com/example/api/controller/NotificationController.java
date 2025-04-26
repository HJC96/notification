package com.example.api.controller;

import com.example.api.dto.NotificationRequest;
import com.example.api.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Notification API", description = "알림 관련 API")
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * 알림 생성 요청을 처리하는 엔드포인트
     */
    @Operation(summary = "알림 전송", description = "알림을 특정 사용자에게 전송합니다.")
    @PostMapping
    public ResponseEntity<Void> sendNotification(@RequestBody NotificationRequest request) {
        notificationService.sendNotification(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}