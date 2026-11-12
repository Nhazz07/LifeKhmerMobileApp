package com.lifekhmer.nutrition.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "foods")
@Getter
@Setter
@NoArgsConstructor
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(name = "normalized_name", nullable = false, unique = true, length = 150)
    private String normalizedName;

    @Column(name = "serving_unit", nullable = false, length = 20)
    private String servingUnit = "g";

    @Column(name = "calories_per_100g", nullable = false)
    private BigDecimal caloriesPer100g;

    @Column(name = "protein_per_100g", nullable = false)
    private BigDecimal proteinPer100g;

    @Column(name = "carbohydrates_per_100g", nullable = false)
    private BigDecimal carbohydratesPer100g;

    @Column(name = "fat_per_100g", nullable = false)
    private BigDecimal fatPer100g;

    @Column(name = "sugar_per_100g", nullable = false)
    private BigDecimal sugarPer100g;

    @Column(name = "fiber_per_100g", nullable = false)
    private BigDecimal fiberPer100g;

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