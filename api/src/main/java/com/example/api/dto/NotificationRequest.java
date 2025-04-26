package com.example.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationRequest {
    private Long userId;
    private String title;
    private String body;
}