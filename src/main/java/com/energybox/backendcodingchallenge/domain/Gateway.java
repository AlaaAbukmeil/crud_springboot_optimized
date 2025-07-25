package com.energybox.backendcodingchallenge.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name="gateways", schema = "devices")
public class Gateway {
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

    @OneToMany(
            mappedBy = "gateway"
    )
    private Set<Sensor> sensors = new HashSet<>();

}
