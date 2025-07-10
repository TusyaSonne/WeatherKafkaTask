package ru.dzhenbaz.WeatherKafkaTask.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.dzhenbaz.WeatherKafkaTask.model.WeatherData;
import ru.dzhenbaz.WeatherKafkaTask.service.WeatherAnalyticsService;

@Component
public class WeatherConsumer {

    private final WeatherAnalyticsService analyticsService;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public WeatherConsumer(WeatherAnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @KafkaListener(topics = "${weather.topic.name}", groupId = "weather-group")
    public void listen(String message) {
        try {
            WeatherData weatherData = objectMapper.readValue(message, WeatherData.class);
            analyticsService.accept(weatherData);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
