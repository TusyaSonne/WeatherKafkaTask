package ru.dzhenbaz.WeatherKafkaTask.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.dzhenbaz.WeatherKafkaTask.model.WeatherData;

import java.util.Random;

@Service
public class WeatherProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    @Value("${weather.topic.name}")
    private final String topic;
    private final Random random = new Random();
    private final String[] conditions = {"солнечно", "облачно", "дождь"};


    public WeatherProducer(KafkaTemplate<String, String> kafkaTemplate, @Value("${weather.topic.name}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Scheduled(fixedRate = 2000)
    public void sendWeatherData() {
        WeatherData data = new WeatherData(random.nextInt(36), conditions[random.nextInt(conditions.length)]);
        kafkaTemplate.send(topic, data.toString());
        System.out.println("Отправлено: " + data);
    }
}
