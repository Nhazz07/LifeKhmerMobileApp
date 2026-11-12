package com.lifekhmer.nutrition.dto.request;

import com.lifekhmer.nutrition.entity.MealType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record MealRequest(
        @NotNull
        UUID foodId,

        @NotNull
        MealType mealType,

        @NotNull
        @DecimalMin("0.01")
        @DecimalMax("5000")
        BigDecimal portionGrams,

        @NotNull
        @PastOrPresent
        LocalDateTime consumedAt,

        @Size(max = 1000)
        String notes
) {
}