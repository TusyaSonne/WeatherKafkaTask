package ru.dzhenbaz.WeatherKafkaTask.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.dzhenbaz.WeatherKafkaTask.service.WeatherAnalyticsService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WeatherAnalyticsServiceTest {

    private WeatherAnalyticsService weatherAnalyticsService;

    @BeforeEach
    void setUp() {
        weatherAnalyticsService = new WeatherAnalyticsService();
    }

    @Test
    void testAcceptAndHottestTemperatureTracking() {
        WeatherData data1 = new WeatherData("Магадан", LocalDate.of(2025, 7, 9), 24, "солнечно");
        WeatherData data2 = new WeatherData("Питер", LocalDate.of(2025, 7, 10), 30, "облачно");
        WeatherData data3 = new WeatherData("Чукотка", LocalDate.of(2025, 7, 11), 27, "дождь");

        weatherAnalyticsService.accept(data1);
        weatherAnalyticsService.accept(data2);
        weatherAnalyticsService.accept(data3);

        assertEquals("Питер", weatherAnalyticsService.getHottest().getCity());
        assertEquals(30, weatherAnalyticsService.getHottest().getTemperature());
    }

    @Test
    void testCityDataAggregation() {
        WeatherData data1 = new WeatherData("Чукотка", LocalDate.of(2025, 7, 8), 18, "солнечно");
        WeatherData data2 = new WeatherData("Чукотка", LocalDate.of(2025, 7, 8), 19, "дождь");
        WeatherData data3 = new WeatherData("Чукотка", LocalDate.of(2025, 7, 9), 21, "солнечно");

        weatherAnalyticsService.accept(data1);
        weatherAnalyticsService.accept(data2);
        weatherAnalyticsService.accept(data3);

        List<WeatherData> dataList = weatherAnalyticsService.getCityData("Чукотка");
        long sunnyDays = dataList.stream().filter(w -> w.getCondition().equals("солнечно")).count();
        assertEquals(2,sunnyDays);
    }
}
