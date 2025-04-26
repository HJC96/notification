package com.example.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationRequest {
    private Long userId;
    private String title;
    private String body;

    public NotificationRequest(Long userId, String title, String body) {
        this.userId = userId;
        this.title = title;
        this.body = body;
    }
}