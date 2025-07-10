package ru.dzhenbaz.WeatherKafkaTask.model;

import java.time.LocalDate;

public class WeatherData {
    private String city;
    private LocalDate date;
    private int temperature;
    private String condition;

    public WeatherData() {}

    public WeatherData(String city, LocalDate date, int temperature, String condition) {
        this.city = city;
        this.date = date;
        this.temperature = temperature;
        this.condition = condition;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        return String.format("Дата: %s, Город: %s, Температура: %d°C, Состояние: %s",
                date, city, temperature, condition);
    }
}
