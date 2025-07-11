package ru.dzhenbaz.WeatherKafkaTask.kafka;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.dzhenbaz.WeatherKafkaTask.service.WeatherAnalyticsService;

@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
public class KafkaIntegrationTest {
    @Container
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.5.0"));

    @DynamicPropertySource
    static void overrideKafkaProps(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private WeatherAnalyticsService analyticsService;

    @Test
    void testKafkaMessageFlow() throws Exception {
        String message = """
            {
              "city": "Магадан",
              "date": "2025-07-10",
              "temperature": 28,
              "condition": "солнечно"
            }
        """;

        kafkaTemplate.send("weather-topic", message);
        kafkaTemplate.flush();

        Thread.sleep(2000);

        analyticsService.printStats(); // пока — выводим в консоль
        // если есть аналитика - данные с продюсера приходят в консюмер и обрабатываются сервисом
    }
}
