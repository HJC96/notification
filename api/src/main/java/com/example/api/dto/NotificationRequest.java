package com.example.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationRequest {
    @Schema(example = "1", description = "사용자 ID")
    private Long userId;
    @Schema(example = "알림 제목", description = "알림 제목")
    private String title;
    @Schema(example = "알림 내용", description = "알림 내용")
    private String body;

    public NotificationRequest(Long userId, String title, String body) {
        this.userId = userId;
        this.title = title;
        this.body = body;
    }
}