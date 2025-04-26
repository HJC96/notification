package com.example.consumer.service;

import org.springframework.stereotype.Service;

@Service
public class PushService {
    public void sendPush(String deviceToken, String title, String body) {
        System.out.println("가상 Push 전송: deviceToken=" + deviceToken +
                " / title=" + title + " / body=" + body);
    }
}

