// NotificationService.java (Interface)
package com.example.api.service;

import com.example.api.dto.request.EmailNotificationRequest;
import com.example.api.dto.request.PushNotificationRequest;
import com.example.api.dto.request.SmsNotificationRequest;
import org.springframework.stereotype.Service;

public interface NotificationService {

    void sendEmailNotification(EmailNotificationRequest request);

    void sendPushNotification(PushNotificationRequest request);

    void sendSmsNotification(SmsNotificationRequest request);
}
