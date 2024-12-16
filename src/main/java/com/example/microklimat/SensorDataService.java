package com.example.microklimat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SensorDataService {

    @Autowired
    private SensorDataRepository repository;

    public void saveSensorData(SensorData data) {
        repository.save(data);
    }

    public List<SensorData> getAllSensorData() {
        return repository.findAll();
    }

    public List<SensorData> getSensorDataForPeriod(long start, long end) {
        return repository.findAll().stream()
                .filter(data -> data.getTimestamp() >= start && data.getTimestamp() <= end)
                .toList();
    }
}
