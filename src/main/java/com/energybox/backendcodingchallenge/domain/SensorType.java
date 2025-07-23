package com.energybox.backendcodingchallenge.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;


@Data
@Entity
@Table(schema = "devices", name = "sensor_types")
public class SensorType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String type;

    @ManyToMany(mappedBy = "types")
    private Set<Sensor> sensors = new HashSet<>();

}
