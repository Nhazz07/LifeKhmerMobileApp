package com.lifekhmer.nutrition.entity;

import com.lifekhmer.accounts.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "meals")
@Getter
@Setter
@NoArgsConstructor
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id")
    private Food food;

    @Enumerated(EnumType.STRING)
    @Column(name = "meal_type", nullable = false, length = 20)
    private MealType mealType;

    @Column(name = "food_name", nullable = false, length = 150)
    private String foodName;

    @Column(name = "portion_grams", nullable = false)
    private BigDecimal portionGrams;

    @Column(nullable = false)
    private BigDecimal calories;

    @Column(nullable = false)
    private BigDecimal protein;

    @Column(nullable = false)
    private BigDecimal carbohydrates;

    @Column(nullable = false)
    private BigDecimal fat;

    @Column(nullable = false)
    private BigDecimal sugar;

    @Column(nullable = false)
    private BigDecimal fiber;

    @Column(name = "consumed_at", nullable = false)
    private LocalDateTime consumedAt;

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