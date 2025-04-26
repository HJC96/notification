package com.example.api.config;

import com.example.core.domain.UserNotificationMetadata;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, UserNotificationMetadata> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, UserNotificationMetadata> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // key는 String으로 저장
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // value는 JSON으로 저장
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }
}