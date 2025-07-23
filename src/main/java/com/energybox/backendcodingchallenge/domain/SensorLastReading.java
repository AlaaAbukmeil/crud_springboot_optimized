package com.energybox.backendcodingchallenge.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Entity
@Table(schema = "devices", name = "sensor_last_readings")
public class SensorLastReading {

    @Id
    private Long sensorId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "sensor_id")
    private Sensor sensor;

    @Column(name = "reading_time", nullable = false)
    private Instant readingTime;

    @Column(name = "reading_value", nullable = false, precision = 19, scale = 6)
    private BigDecimal readingValue;

}
