package com.energybox.backendcodingchallenge.service;

import com.energybox.backendcodingchallenge.domain.SensorLastReading;
import com.energybox.backendcodingchallenge.repository.SensorLastReadingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@Transactional
@RequiredArgsConstructor
public class SensorLastReadingService {
    private final SensorLastReadingRepository repo;

    public SensorLastReading create(SensorLastReading sensorLastReading) {
        return repo.save(sensorLastReading);
    }


}
