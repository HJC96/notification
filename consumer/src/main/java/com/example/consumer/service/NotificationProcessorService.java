package com.example.consumer.service;

import com.example.core.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationProcessorService {

    public void process(NotificationEvent event) {
        // 여기에 "실제 후처리"를 작성하는거야
        System.out.println("알림을 보낸다: " + event.getTitle() + " - " + event.getBody());

        // 예를 들어, 알림 서버 호출, DB 업데이트, 통계 적재 등
    }
}