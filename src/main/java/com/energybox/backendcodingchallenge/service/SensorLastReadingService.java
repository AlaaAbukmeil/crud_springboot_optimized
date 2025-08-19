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
import org.springframework.data.domain.Sort;
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
    public SensorLastReading create(SensorLastReadingRequest request) {
        Sensor sensor = sensorRepository.findById(request.getSensorId())
                .orElseThrow(() -> new EntityNotFoundException("Sensor not found with id: " + request.getSensorId()));

        SensorType sensorType = sensorTypeRepository.findByType(request.getSensorType())
                .orElseThrow(() -> new EntityNotFoundException("Sensor type not found: " + request.getSensorType()));

        System.out.println(request.getSensorType());
        if (!sensor.getTypes().contains(sensorType)) {
            System.out.println("yes");

            throw new EntityNotFoundException("Sensor with id: " +request.getSensorId() + " does not have sensor type: " + request.getSensorType());
        }


        Optional<SensorLastReading> existingReading = repo.findBySensorIdAndSensorTypeId(
                request.getSensorId(),
                sensorType.getId()
        );

        SensorLastReading sensorLastReading;

        if (existingReading.isPresent()) {
            // Update existing reading
            sensorLastReading = existingReading.get();
            sensorLastReading.setReadingValue(request.getReadingValue());

            // Update reading time if provided, otherwise keep existing or set current time
            if (request.getReadingTime() != null) {
                sensorLastReading.setReadingTime(request.getReadingTime());
            } else {
                sensorLastReading.setReadingTime(Instant.now());
            }
        } else {
            sensorLastReading = new SensorLastReading();
            sensorLastReading.setSensor(sensor);
            sensorLastReading.setSensorType(sensorType);
            sensorLastReading.setReadingValue(request.getReadingValue());

            if (request.getReadingTime() != null) {
                sensorLastReading.setReadingTime(request.getReadingTime());
            }
        }

        return repo.save(sensorLastReading);
    }

    public List<SensorLastReading> findBySensorId(Long sensorId){
        List<SensorLastReading> result = repo.findBySensorId(sensorId);
        return result;
    }
    public List<SensorLastReading> findAll(){
        List<SensorLastReading> result = repo.findAll(Sort.by("sensor_id"));
        return result;
    }




}
