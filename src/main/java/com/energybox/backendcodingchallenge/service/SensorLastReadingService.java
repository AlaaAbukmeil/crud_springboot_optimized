package com.energybox.backendcodingchallenge.service;

import com.energybox.backendcodingchallenge.domain.Sensor;
import com.energybox.backendcodingchallenge.domain.SensorLastReading;
import com.energybox.backendcodingchallenge.repository.SensorLastReadingRepository;
import com.energybox.backendcodingchallenge.repository.SensorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class SensorLastReadingService {
    private final SensorLastReadingRepository repo;
    private final SensorRepository sensorRepository;
    public SensorLastReading create(SensorLastReading sensorLastReading) {
        return repo.save(sensorLastReading);
    }

    public List<SensorLastReading> findBySensorId(Long sensorId){
        Optional<Sensor> sensor = sensorRepository.findById(sensorId);
        List<SensorLastReading> result = repo.findBySensorId(sensorId);
        return result;
    }

}
