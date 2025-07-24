package com.energybox.backendcodingchallenge.repository;

import com.energybox.backendcodingchallenge.domain.SensorLastReading;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SensorLastReadingRepository extends JpaRepository<SensorLastReading, Long> {
    List<SensorLastReading> findBySensorId(Long sensorId);
}
