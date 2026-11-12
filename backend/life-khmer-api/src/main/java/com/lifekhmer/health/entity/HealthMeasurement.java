package com.lifekhmer.health.entity;

import com.lifekhmer.accounts.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "health_measurements")
@Getter
@Setter
@NoArgsConstructor
public class HealthMeasurement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "measurement_type", nullable = false, length = 30)
    private MeasurementType measurementType;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal value;

    @Column(name = "secondary_value", precision = 10, scale = 2)
    private BigDecimal secondaryValue;

    @Column(nullable = false, length = 20)
    private String unit;

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt;

    @Column(length = 1000)
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void create() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    void update() {
        updatedAt = LocalDateTime.now();
    }
}