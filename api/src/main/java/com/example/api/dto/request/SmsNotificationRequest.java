package com.example.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SmsNotificationRequest {

    @Schema(example = "1", description = "사용자 ID")
    private Long userId;

    @Schema(example = "문자 메시지 내용", description = "SMS 본문")
    private String message;
}