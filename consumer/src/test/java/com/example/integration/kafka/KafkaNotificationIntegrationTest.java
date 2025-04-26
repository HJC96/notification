package com.example.integration.kafka;

import com.example.consumer.ConsumerApplication;
import com.example.core.event.NotificationEvent;
import com.example.core.event.NotificationType;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Kafka Notification 통합 테스트
 */
@SpringBootTest(classes = ConsumerApplication.class)  // 이걸 명시!
@EmbeddedKafka(partitions = 1, topics = {"notification-topic"})
@ActiveProfiles("test")
@Testcontainers
class KafkaNotificationIntegrationTest {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Test
    void notificationEvent가_정상적으로_발행되고_소비된다() {
        // 1. KafkaTemplate 직접 생성
        Map<String, Object> producerProps = KafkaTestUtils.producerProps(embeddedKafkaBroker);
        ProducerFactory<String, NotificationEvent> producerFactory =
                new DefaultKafkaProducerFactory<>(producerProps, new StringSerializer(), new JsonSerializer<>());

        KafkaTemplate<String, NotificationEvent> kafkaTemplate = new KafkaTemplate<>(producerFactory);

        // 2. 이벤트 생성
        NotificationEvent event = NotificationEvent.builder()
                .userId(1L)
                .title("Test Title")
                .body("Test Body")
                .targetToken("test-device-token")
                .type(NotificationType.PUSH)
                .build();

        // 3. Kafka 전송
        kafkaTemplate.send("notification-topic", event);

        // 4. Consumer로 메시지 검증
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("test-group", "false", embeddedKafkaBroker);
        KafkaConsumer<String, NotificationEvent> consumer = new KafkaConsumer<>(
                consumerProps,
                new StringDeserializer(),
                new JsonDeserializer<>(NotificationEvent.class, false)
        );

        embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumer, "notification-topic");

        ConsumerRecord<String, NotificationEvent> received = KafkaTestUtils.getSingleRecord(consumer, "notification-topic");

        assertThat(received).isNotNull();
        assertThat(received.value().getTitle()).isEqualTo("Test Title");
        assertThat(received.value().getBody()).isEqualTo("Test Body");
    }
}