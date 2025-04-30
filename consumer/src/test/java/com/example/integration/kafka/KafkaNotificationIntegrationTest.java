package com.example.integration.kafka;

import com.example.consumer.ConsumerApplication;
import com.example.consumer.config.MailConfig;
import com.example.consumer.consumer.NotificationEventConsumer;
import com.example.core.event.NotificationEvent;
import com.example.core.event.NotificationType;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

//@SpringBootTest(classes = {ConsumerApplication.class, KafkaTestConfig.class, MailConfig.class})
@SpringBootTest(classes = {ConsumerApplication.class, MailConfig.class})
@EmbeddedKafka(partitions = 1, topics = {"notification-event"})
@ActiveProfiles("test")
//@Testcontainers
class KafkaNotificationIntegrationTest {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private NotificationEventConsumer consumer;

    @Autowired
    private KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    @Autowired
    private JavaMailSender javaMailSender;

    private final Long userId = 1L;


    @BeforeEach
    void setUp() {
        // 1. KafkaTemplate 직접 생성
        Map<String, Object> producerProps = KafkaTestUtils.producerProps(embeddedKafkaBroker);
        ProducerFactory<String, NotificationEvent> producerFactory =
                new DefaultKafkaProducerFactory<>(producerProps, new StringSerializer(), new JsonSerializer<>());

        kafkaTemplate = new KafkaTemplate<>(producerFactory);
        redisTemplate.delete("rate_limit:user:" + userId);
        consumer.resetHandled();
    }

    @Test
    void notificationEvent가_정상적으로_Consumer에서_처리된다() throws Exception {
        NotificationEvent event = NotificationEvent.builder()
                .userId(1L)
                .notificationType(NotificationType.SMS)
                .title("Test Title")
                .content("Test Content")
                .deviceToken("device-token")
                .build();

        // when
        kafkaTemplate.send("notification-event", event);

        // then
        await()
                .atMost(20, TimeUnit.SECONDS)
                .untilAsserted(() -> assertThat(consumer.isHandled()).isTrue());
    }


    /* embeddedKafkaBroker 사용 테스트 */
    @Test
    void notificationEvent가_정상적으로_발행되고_소비된다() {

        // 2. 이벤트 생성
        NotificationEvent event = NotificationEvent.builder()
                .notificationType(NotificationType.SMS)
                .title("Test Title")
                .content("Test Content")
                .deviceToken("test-device-token")
                .build();

        // 3. Kafka 전송
        kafkaTemplate.send("notification-event", event);

        // 4. Consumer로 메시지 검증
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("test-group", "false", embeddedKafkaBroker);
        KafkaConsumer<String, NotificationEvent> consumer = new KafkaConsumer<>(
                consumerProps,
                new StringDeserializer(),
                new JsonDeserializer<>(NotificationEvent.class, false)
        );

        embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumer, "notification-event");

        ConsumerRecord<String, NotificationEvent> received = KafkaTestUtils.getSingleRecord(consumer, "notification-event");

        assertThat(received).isNotNull();
        assertThat(received.value().getTitle()).isEqualTo("Test Title");
        assertThat(received.value().getContent()).isEqualTo("Test Content");
    }

    @Test
    void 초당5회_이상_전송시_차단되어야_한다() throws InterruptedException {
        NotificationEvent event = NotificationEvent.builder()
                .userId(userId)
                .notificationType(NotificationType.SMS)
                .title("Rate Limit Test")
                .content("내용")
                .recipientPhone("010-1234-5678")
                .build();

        // 10개 이벤트를 빠르게 전송
        for (int i = 1; i <= 10; i++) {
            kafkaTemplate.send("notification-event", event);
            Thread.sleep(50); // 50ms 간격으로 전송 => 1초 이내 10건
        }

        Thread.sleep(3000); // Kafka Listener가 처리할 시간

        // consumer가 몇 번 처리했는지 확인 (AtomicInteger로 바꾸는 게 더 정밀함)
        int handledCount = consumer.getHandledCount();
        assertEquals(5, handledCount, "정상적으로 처리된 요청은 최대 5건이어야 한다");
    }
}