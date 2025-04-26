package com.example.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PushNotificationRequest {

    @Schema(example = "1", description = "사용자 ID")
    private Long userId;

    @Schema(example = "푸시 알림 제목", description = "푸시 알림 제목")
    private String title;

    @Schema(example = "푸시 알림 내용", description = "푸시 알림 내용")
    private String body;
}