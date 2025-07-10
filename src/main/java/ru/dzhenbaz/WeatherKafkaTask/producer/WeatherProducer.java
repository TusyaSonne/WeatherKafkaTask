package ru.dzhenbaz.WeatherKafkaTask.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.dzhenbaz.WeatherKafkaTask.model.WeatherData;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Service
public class WeatherProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topic;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    private final Random random = new Random();

    private final List<String> cities = List.of("Магадан", "Питер", "Чукотка", "Тюмень");
    private final List<String> conditions = List.of("солнечно", "облачно", "дождь");


    public WeatherProducer(KafkaTemplate<String, String> kafkaTemplate, @Value("${weather.topic.name}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Scheduled(fixedRate = 2000)
    public void sendWeatherData() {
        WeatherData data = generateRandomWeatherData();
        try {
            String json = objectMapper.writeValueAsString(data);
            kafkaTemplate.send(topic, json);
            System.out.println("Отправлено: " + data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public WeatherData generateRandomWeatherData() {
        String city = cities.get(random.nextInt(cities.size()));
        LocalDate date = LocalDate.now().minusDays(random.nextInt(7));
        int temperature = random.nextInt(36);
        String condition = conditions.get(random.nextInt(conditions.size()));
        return new WeatherData(city, date, temperature, condition);
    }
}
