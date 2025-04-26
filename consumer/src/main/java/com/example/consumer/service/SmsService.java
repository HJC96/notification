package com.example.consumer.service;

import org.springframework.stereotype.Service;

@Service
public class SmsService {
    public void sendSms(String phoneNumber, String message) {
        // 실제 SMS API 호출 로직 작성

        // 예시) 외부 API 호출 (RestTemplate 사용)
        // restTemplate.postForEntity(smsApiUrl, 요청객체, 응답객체.class);

        System.out.println(" 가상 SMS 전송: " + phoneNumber + " / " + message);

        // 실제로는 HTTP 요청 보내야 하고,
        // 실패 시 예외 던지는 로직도 들어가야 해요.
    }
}