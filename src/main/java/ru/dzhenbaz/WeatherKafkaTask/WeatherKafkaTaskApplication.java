package ru.dzhenbaz.WeatherKafkaTask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // для выполнения методов по расписанию
public class WeatherKafkaTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeatherKafkaTaskApplication.class, args);
	}

}
