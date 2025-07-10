FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/WeatherKafkaTask-*.jar weather-kafka-task.jar

ENTRYPOINT ["java", "-jar", "weather-kafka-task.jar"]

