package com.lifekhmer.foodanalysis.dto.request;

import com.lifekhmer.nutrition.entity.MealType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ConfirmFoodAnalysisRequest(
        @NotNull(message = "Meal type is required")
        MealType mealType,

        @Size(max = 150)
        String customFoodName,

        @NotNull(message = "Portion in grams is required")
        @Positive(message = "Portion must be greater than zero")
        BigDecimal portionGrams,

        LocalDateTime consumedAt,

        @Size(max = 1000)
        String notes
) {}
