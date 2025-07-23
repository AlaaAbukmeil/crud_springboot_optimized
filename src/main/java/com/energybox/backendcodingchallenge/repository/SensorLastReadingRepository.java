package com.energybox.backendcodingchallenge.repository;

import com.energybox.backendcodingchallenge.domain.SensorLastReading;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SensorLastReadingRepository extends JpaRepository<SensorLastReading, Long> {
    Optional<SensorLastReading> findBySensorId(Long sensorId);
}
