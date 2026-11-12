package com.lifekhmer.foodanalysis.entity;

import com.lifekhmer.accounts.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "food_analyses")
@Getter
@Setter
@NoArgsConstructor
public class FoodAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "image_url", length = 1024)
    private String imageUrl;

    @Column(name = "detected_food_name", nullable = false, length = 150)
    private String detectedFoodName;

    @Column(precision = 5, scale = 2)
    private BigDecimal confidence;

    @Column(nullable = false, length = 30)
    private String status = "COMPLETED"; // PENDING, COMPLETED, CONFIRMED, FAILED

    @Column(name = "estimated_calories", nullable = false, precision = 10, scale = 2)
    private BigDecimal estimatedCalories;

    @Column(name = "estimated_protein", nullable = false, precision = 10, scale = 2)
    private BigDecimal estimatedProtein;

    @Column(name = "estimated_carbohydrates", nullable = false, precision = 10, scale = 2)
    private BigDecimal estimatedCarbohydrates;

    @Column(name = "estimated_fat", nullable = false, precision = 10, scale = 2)
    private BigDecimal estimatedFat;

    @Column(name = "suggested_portion_grams", nullable = false, precision = 10, scale = 2)
    private BigDecimal suggestedPortionGrams = BigDecimal.valueOf(200);

    @Column(name = "raw_ai_response", columnDefinition = "TEXT")
    private String rawAiResponse;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
