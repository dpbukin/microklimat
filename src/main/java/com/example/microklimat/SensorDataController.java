package com.example.microklimat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
@Controller
public class SensorDataController {

    @Autowired
    private SensorDataService service;

    // Главная страница с отображением данных
    @GetMapping("/index")
    public String getMainPage(Model model) {
        model.addAttribute("data", service.getAllSensorData());
        return "index";  // Страница с вашим HTML
    }

    @PostMapping("/sensorData")
    public ResponseEntity<String> receiveSensorData(@RequestBody SensorData sensorData) {
//        System.out.println("Получены данные от датчика: " + sensorData);

        try {
            service.saveSensorData(sensorData);
//            System.out.println("Данные успешно сохранены: " + sensorData);
        } catch (Exception e) {
//            System.err.println("Ошибка при сохранении данных: " + e.getMessage());
            e.printStackTrace();
        }

        return ResponseEntity.ok("Данные успешно получены");
    }

    // Страница для скачивания отчета
    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadReport() {
        List<SensorData> data = service.getAllSensorData();

        // Создаем временный файл для отчета
        try {
            File file = new File("sensor_report.csv");
            try (FileWriter writer = new FileWriter(file)) {
                writer.append("Room ID, Temperature, Humidity, Timestamp\n");
                for (SensorData sensorData : data) {
                    writer.append(sensorData.getRoomId())
                            .append(", ")
                            .append(String.valueOf(sensorData.getTemperature()))
                            .append(", ")
                            .append(String.valueOf(sensorData.getHumidity()))
                            .append(", ")
                            .append(String.valueOf(sensorData.getTimestamp()))
                            .append("\n");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"sensor_report.csv\"")
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .body(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    // Страница с таблицей данных и кнопкой для скачивания отчета
    @GetMapping("/sensorDataPage")
    public String getSensorDataPage(Model model) {
        List<SensorData> data = service.getLastTenSensorData();
        model.addAttribute("data", data);
        return "sensorDataPage";  // Новая страница с таблицей и кнопкой
    }
}
