package ru.dzhenbaz.WeatherKafkaTask.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class WeatherConsumer {
    @KafkaListener(topics = "${weather.topic.name}", groupId = "weather-group")
    public void listen(String message) {
        System.out.println("Получено: " + message);
    }
}
