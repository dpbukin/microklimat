package com.example.microklimat;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class SensorData {

    private String roomId;
    private double temperature;
    private double humidity;
    private long timestamp;

    public SensorData() {}

    public SensorData(String roomId, double temperature, double humidity, long timestamp) {
        this.roomId = roomId;
        this.temperature = temperature;
        this.humidity = humidity;
        this.timestamp = timestamp;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}