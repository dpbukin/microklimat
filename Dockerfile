# Используем официальный образ Java 17
FROM openjdk:17-jdk-slim

# Устанавливаем рабочую директорию в контейнере
WORKDIR /app

# Копируем jar-файл в контейнер
COPY target/floorplan-1.0.0.jar /app/floorplan.jar

# Открываем порт 8080 для приложения
EXPOSE 8080

# Запускаем приложение при старте контейнера
CMD ["java", "-jar", "floorplan.jar"]