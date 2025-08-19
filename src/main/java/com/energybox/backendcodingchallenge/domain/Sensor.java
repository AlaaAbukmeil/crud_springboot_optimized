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

    @Column(name = "x_coordinate", precision = 10, scale = 6)
    private BigDecimal xCoordinate;

    @Column(name = "y_coordinate", precision = 10, scale = 6)
    private BigDecimal yCoordinate;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "gateway_id", insertable = false, updatable = false)
    private Long gatewayId;


    //    lazy so gateway is not queried, if gateway is deleted, then gateway id is just null
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gateway_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @ToString.Exclude
    private Gateway gateway;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JoinTable(
            schema = "devices",
            name = "sensor_id_with_type_id",
            joinColumns = @JoinColumn(name = "sensor_id"),
            inverseJoinColumns = @JoinColumn(name = "sensor_type_id")
    )
    private Set<SensorType> types = new HashSet<>();

}
