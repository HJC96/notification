package com.example.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailNotificationRequest {

    @Schema(example = "1", description = "사용자 ID")
    private Long userId;

    @Schema(example = "회원가입 완료", description = "이메일 제목")
    private String subject;

    @Schema(example = "회원가입을 축하합니다!", description = "이메일 본문")
    private String body;
}