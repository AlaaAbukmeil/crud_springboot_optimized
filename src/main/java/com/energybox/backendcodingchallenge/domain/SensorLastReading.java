package com.energybox.backendcodingchallenge.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Entity
@Table(schema = "devices", name = "sensor_last_readings",
        uniqueConstraints = @UniqueConstraint(
        columnNames = {"sensor_id","sensor_type_id"}
))
public class SensorLastReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sensor_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Sensor sensor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sensor_type_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private SensorType sensorType;

    @Column(name = "reading_time", nullable = false)
    @CreationTimestamp
    private Instant readingTime;

    @Column(name = "reading_value", nullable = false, precision = 19, scale = 6)
    private BigDecimal readingValue;

}
