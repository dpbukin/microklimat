package com.example.microklimat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.TextMessage;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SensorDataWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private SensorDataService service;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        // Получаем данные от клиента (например, запрос на получение данных)
        System.out.println("Received message: " + message.getPayload());

        // Получаем последние данные из базы
        List<SensorData> sensorData = service.getAllSensorData();

        // Формируем данные для отправки клиенту
        Map<String, Object> response = new HashMap<>();
        for (SensorData data : sensorData) {
            response.put(data.getRoomId(), Map.of(
                    "temp", data.getTemperature(),
                    "hum", data.getHumidity()
            ));
        }

        // Отправляем данные обратно клиенту
        session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(response)));
    }
}
