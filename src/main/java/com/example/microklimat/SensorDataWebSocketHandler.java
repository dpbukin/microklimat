package com.example.microklimat;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.TextMessage;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SensorDataWebSocketHandler extends TextWebSocketHandler {

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        // Пример получения данных от Arduino
        System.out.println("Received message: " + message.getPayload());

        // Обработка и отправка данных обратно клиенту (например, обновление данных о температуре и влажности)
        session.sendMessage(new TextMessage("Data updated"));

        // Здесь мы можем сохранить данные в MongoDB (если они не пустые)
    }
}
