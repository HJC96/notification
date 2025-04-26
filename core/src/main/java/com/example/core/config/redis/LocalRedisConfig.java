package com.example.core.config.redis;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * 테스트 환경 (test 프로필)에서 Redis를 테스트컨테이너로 띄우는 설정
 */
@Profile("test")
@Slf4j
@Configuration
public class LocalRedisConfig {

    private static final String IMAGE = "redis:7.4.0";
    private static final int PORT = 6379;
    private GenericContainer<?> redisContainer;

    /**
     * 테스트 시작 시 Redis TestContainer 기동
     */
    @PostConstruct
    public void startRedis() {
        try {
            redisContainer = new GenericContainer<>(DockerImageName.parse(IMAGE))
                    .withExposedPorts(PORT);
            redisContainer.start();
        } catch (Exception e) {
            log.error("[Redis TestContainer] Redis 시작 실패", e);
        }
    }

    /**
     * 테스트 종료 시 Redis TestContainer 중지
     */
    @PreDestroy
    public void stopRedis() {
        if (redisContainer != null) {
            try {
                redisContainer.stop();
            } catch (Exception e) {
                log.error("[Redis TestContainer] Redis 종료 실패", e);
            }
        }
    }

    /**
     * Redis 연결을 위한 LettuceConnectionFactory 빈 등록
     * TestContainer로 기동한 Redis에 연결
     */
    @Bean(name = "redisConnectionFactory")
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(
                redisContainer.getHost(),
                redisContainer.getMappedPort(PORT)
        );
        return new LettuceConnectionFactory(config);
    }
}