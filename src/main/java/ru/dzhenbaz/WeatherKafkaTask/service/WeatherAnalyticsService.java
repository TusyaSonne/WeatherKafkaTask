package ru.dzhenbaz.WeatherKafkaTask.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.dzhenbaz.WeatherKafkaTask.model.WeatherData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WeatherAnalyticsService {

    private final Map<String, List<WeatherData>> cityDataMap = new HashMap<>();
    private WeatherData hottest = null;

    public synchronized void accept(WeatherData weatherData) {
        cityDataMap.computeIfAbsent(weatherData.getCity(), k -> new ArrayList<>()).add(weatherData);

        if (hottest == null || weatherData.getTemperature() > hottest.getTemperature()) {
            hottest = weatherData;
        }
    }

    @Scheduled(fixedRate = 10000)
    public synchronized void printStats() {
        System.out.println("\n Аналитика за последнее время:");

        for (Map.Entry<String, List<WeatherData>> entry : cityDataMap.entrySet()) {
            String city = entry.getKey();
            List<WeatherData> entries = entry.getValue();

            long sunny = entries.stream().
                    filter(e -> e.getCondition().equals("солнечно"))
                    .map(WeatherData::getDate)
                    .distinct()
                    .count();

            long rainy = entries.stream()
                    .filter(e -> e.getCondition().equals("дождь"))
                    .distinct()
                    .count();

            double avgTemp = entries.stream()
                    .mapToInt(WeatherData::getTemperature)
                    .average()
                    .orElse(0);

            System.out.printf("- %s: %d солнечных дней, %d дождей, средняя температура %.1f°C%n",
                    city, sunny, rainy, avgTemp);
        }

        if (hottest != null) {
            System.out.printf(" Самый жаркий день: %s в %s (%.1f°C)%n",
                    hottest.getDate(), hottest.getCity(), (double) hottest.getTemperature());
        }
    }
}
