package com.example.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailNotificationRequest {

    @Schema(example = "1", description = "사용자 ID")
    private Long userId;

    @Schema(example = "패스워드 리셋", description = "이메일 제목")
    private String subject;

    @Schema(
            example = "{\"name\": \"지찬님\", \"link\": \"https://example.com/reset\"}",
            description = "이메일 본문 치환용 변수들 (키-값 쌍)"
    )
    private Map<String, String> variables;

    @Schema(example = "email-template:reset-password", description = "템플릿 키")
    private String templateKey;
}