package com.energybox.backendcodingchallenge.service;

import com.energybox.backendcodingchallenge.domain.Sensor;
import com.energybox.backendcodingchallenge.domain.SensorLastReading;
import com.energybox.backendcodingchallenge.domain.SensorType;
import com.energybox.backendcodingchallenge.dto.request.SensorLastReadingRequest;
import com.energybox.backendcodingchallenge.dto.response.SensorLastReadingResponse;
import com.energybox.backendcodingchallenge.repository.SensorLastReadingRepository;
import com.energybox.backendcodingchallenge.repository.SensorRepository;
import com.energybox.backendcodingchallenge.repository.SensorTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class SensorLastReadingService {
    private final SensorLastReadingRepository repo;
    private final SensorRepository sensorRepository;
    private final SensorTypeRepository sensorTypeRepository;
    public SensorLastReading create(SensorLastReading sensorLastReading) {
        return repo.save(sensorLastReading);
    }

    public List<SensorLastReading> findBySensorId(Long sensorId){
        Optional<Sensor> sensor = sensorRepository.findById(sensorId);
        List<SensorLastReading> result = repo.findBySensorId(sensorId);
        return result;
    }

    public SensorLastReading createOrUpdateLastReading(SensorLastReadingRequest request) {
        Sensor sensor = sensorRepository.findById(request.getSensorId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Sensor with id " + request.getSensorId() + " not found"));

        SensorType sensorType = sensorTypeRepository.findByType(request.getSensorType())
                .orElseThrow(() -> new EntityNotFoundException(
                        "SensorType '" + request.getSensorType() + "' not found"));

        if (!sensor.getTypes().contains(sensorType)) {
            throw new IllegalArgumentException(
                    "Sensor " + sensor.getName() + " does not support type " + request.getSensorType());
        }

        SensorLastReading existingReading = repo
                .findBySensorIdAndSensorType_Type(request.getSensorId(), request.getSensorType())
                .orElse(null);

        SensorLastReading reading;
        if (existingReading != null) {
            reading = existingReading;
            reading.setReadingValue(request.getReadingValue());
            reading.setReadingTime(request.getReadingTime() != null ?
                    request.getReadingTime() : Instant.now());
        } else {
            reading = new SensorLastReading();
            reading.setSensor(sensor);
            reading.setSensorType(sensorType);
            reading.setReadingValue(request.getReadingValue());
            reading.setReadingTime(request.getReadingTime() != null ?
                    request.getReadingTime() : Instant.now());
        }

        SensorLastReading savedReading = repo.save(reading);
        return savedReading;
    }


}
