package com.example.microklimat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

@Component
public class SensorDataUpdater {

    private final SensorDataWebSocketHandler webSocketHandler;

    @Autowired
    public SensorDataUpdater(SensorDataWebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;

        // Настроить таймер для периодического отправления обновлений
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    // Отправляем обновления всем подключённым клиентам
                    webSocketHandler.sendUpdatedData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 2000);  // Интервал 5 секунд
    }
}
