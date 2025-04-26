package com.example.core.config.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

/**
 * 로컬 개발 환경 (local 프로필)에서 Redis를 연결하는 설정
 * Docker Compose로 띄운 localhost Redis 서버에 연결
 */
@Configuration
public class DockerComposeRedisConfig {

    private static final String REDIS_HOST = "localhost";
    private static final int REDIS_PORT = 6379;

    /**
     * Redis 연결을 위한 LettuceConnectionFactory 빈 등록
     */
    @Bean(name = "redisConnectionFactory")
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(REDIS_HOST, REDIS_PORT);
        return new LettuceConnectionFactory(config);
    }
}