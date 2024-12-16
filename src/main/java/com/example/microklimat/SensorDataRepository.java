package com.example.microklimat;


import org.springframework.data.mongodb.repository.MongoRepository;

public interface SensorDataRepository extends MongoRepository<SensorData, String> {
}
