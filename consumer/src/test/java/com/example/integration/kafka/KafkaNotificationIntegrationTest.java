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

//@SpringBootTest(classes = {ConsumerApplication.class, KafkaTestConfig.class, MailConfig.class})
@SpringBootTest(classes = {ConsumerApplication.class, MailConfig.class})
@EmbeddedKafka(partitions = 1, topics = {"notification-event"})
@ActiveProfiles("test")
//@Testcontainers
class KafkaNotificationIntegrationTest {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private NotificationEventConsumer consumer;

    @Autowired
    private KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    @Autowired
    private JavaMailSender javaMailSender;


    @BeforeEach
    void setUp() {
        // 1. KafkaTemplate 직접 생성
        Map<String, Object> producerProps = KafkaTestUtils.producerProps(embeddedKafkaBroker);
        ProducerFactory<String, NotificationEvent> producerFactory =
                new DefaultKafkaProducerFactory<>(producerProps, new StringSerializer(), new JsonSerializer<>());

        kafkaTemplate = new KafkaTemplate<>(producerFactory);
    }

    /* 테스트 컨테이너 사용 */
    @Test
    void notificationEvent가_정상적으로_Consumer에서_처리된다() throws Exception {
        NotificationEvent event = NotificationEvent.builder()
                .notificationType(NotificationType.PUSH)
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
                .notificationType(NotificationType.PUSH)
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
}