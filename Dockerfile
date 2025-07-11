# сборка
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# запуск
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/WeatherKafkaTask-*.jar weather-kafka-task.jar
ENTRYPOINT ["java", "-jar", "weather-kafka-task.jar"]