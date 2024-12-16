package com.example.microklimat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.TextMessage;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SensorDataWebSocketHandler extends TextWebSocketHandler {

    private final SensorDataService service;

    // Хранение всех подключённых сессий
    private Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Autowired
    public SensorDataWebSocketHandler(SensorDataService service) {
        this.service = service;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Добавление клиента в список при установлении соединения
        sessions.put(session.getId(), session);
//        System.out.println("Новое подключение: " + session.getId());

        // Отправить текущие данные клиенту при подключении
        sendSensorData(session);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String payload = message.getPayload();
//        System.out.println("Получено сообщение по WebSocket: " + payload);

        if ("getData".equals(payload)) {
            // Запрос от клиента на получение данных
            sendSensorData(session);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // Удалить сессию клиента из списка после закрытия соединения
        sessions.remove(session.getId());
//        System.out.println("Подключение закрыто: " + session.getId());
    }

    private void sendSensorData(WebSocketSession session) throws IOException {
        // Получаем последние данные из MongoDB
        List<SensorData> sensorDataList = service.getAllSensorData();
//        System.out.println("Данные из MongoDB для отправки: " + sensorDataList);

        // Формируем данные для отправки клиенту
        Map<String, Object> response = new HashMap<>();
        for (SensorData data : sensorDataList) {
            response.put(data.getRoomId(), Map.of(
                    "temp", data.getTemperature(),
                    "hum", data.getHumidity()
            ));
        }

        String jsonResponse = new ObjectMapper().writeValueAsString(response);
//        System.out.println("Отправка данных клиенту по WebSocket: " + jsonResponse);

        // Отправляем данные только текущему клиенту
        session.sendMessage(new TextMessage(jsonResponse));
    }

    // Метод для отправки обновлений всем подключённым клиентам
    public void sendUpdatedData() throws IOException {
        // Получаем новые данные
        List<SensorData> sensorDataList = service.getAllSensorData();
        Map<String, Object> response = new HashMap<>();
        for (SensorData data : sensorDataList) {
            response.put(data.getRoomId(), Map.of(
                    "temp", data.getTemperature(),
                    "hum", data.getHumidity()
            ));
        }
        String jsonResponse = new ObjectMapper().writeValueAsString(response);

        // Отправляем обновления всем подключённым клиентам
        for (WebSocketSession session : sessions.values()) {
            session.sendMessage(new TextMessage(jsonResponse));
        }
    }

}
