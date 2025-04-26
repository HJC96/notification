package com.example.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.example") // core에 있는 redisTemplate을 못가져와서, com.example로 변경
@EnableJpaRepositories(basePackages = "com.example.core.repository")  // core에 있는 Repository 스캔
@EntityScan(basePackages = "com.example.core.domain")                  // core에 있는 Entity 스캔
public class ApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
}