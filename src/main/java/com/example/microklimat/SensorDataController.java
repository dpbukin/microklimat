package com.example.microklimat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
@Controller
public class SensorDataController {

    @Autowired
    private SensorDataService service;

    // Главная страница с отображением данных
    @GetMapping("/")
    public String getMainPage(Model model) {
        model.addAttribute("data", service.getAllSensorData());
        return "index";  // Страница с вашим HTML
    }

    // Страница для скачивания отчета
    @GetMapping("/download")
    public String downloadReport(Model model, @RequestParam("start") long start, @RequestParam("end") long end) {
        List<SensorData> data = service.getSensorDataForPeriod(start, end);

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
            }

            model.addAttribute("message", "Report generated successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "Error generating report.");
        }

        return "report";  // Страница для вывода сообщения об успехе
    }
}
