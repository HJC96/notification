package com.example.consumer.service;

import org.springframework.stereotype.Service;

@Service
public class SmsService {
    public void sendSms(String phoneNumber, String message) {
        System.out.println(" 가상 SMS 전송: " + phoneNumber + " / " + message);
    }
}