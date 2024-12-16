# Используем официальный образ Java 17
FROM openjdk:17-jdk-slim

# Устанавливаем рабочую директорию в контейнере
WORKDIR /app

# Копируем jar-файл в контейнер
COPY target/microklimat-0.0.1-SNAPSHOT.jar /app/microklimat.jar

# Открываем порт 8080 для приложения
EXPOSE 8080

# Запускаем приложение при старте контейнера
CMD ["java", "-jar", "microklimat.jar"]