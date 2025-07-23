package com.energybox.backendcodingchallenge.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(schema = "devices", name = "sensors")
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

//    lazy so gateway is not queried, if gateway is deleted, then gateway id is just null
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gateway_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @ToString.Exclude
    private Gateway gateway;

    @ManyToMany
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JoinTable(
            schema = "devices",
            name = "sensor_sensor_types",
            joinColumns = @JoinColumn(name = "sensor_id"),
            inverseJoinColumns = @JoinColumn(name = "sensor_type_id")
    )
    private Set<SensorType> types = new HashSet<>();

}
