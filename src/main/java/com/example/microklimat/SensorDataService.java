package com.example.microklimat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SensorDataService {

    @Autowired
    private SensorDataRepository repository;

    // Метод для получения последних 10 записей
    public List<SensorData> getLastTenSensorData() {
        List<SensorData> allData = repository.findAll();  // Получаем все данные
        int size = allData.size();
        return allData.subList(Math.max(size - 10, 0), size);  // Возвращаем последние 10 записей
    }
    public void saveSensorData(SensorData data) {
        System.out.println("Сохранение данных в репозиторий: " + data);
        repository.save(data);
    }

    public List<SensorData> getAllSensorData() {
        List<SensorData> data = repository.findAll();
        System.out.println("Получены данные из репозитория: " + data);
        return data;
    }

}
