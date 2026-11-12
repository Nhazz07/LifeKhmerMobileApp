package com.lifekhmer.nutrition.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record FoodRequest(
        @NotBlank
        @Size(max = 150)
        String name,

        @NotNull
        @DecimalMin("0")
        BigDecimal caloriesPer100g,

        @NotNull
        @DecimalMin("0")
        BigDecimal proteinPer100g,

        @NotNull
        @DecimalMin("0")
        BigDecimal carbohydratesPer100g,

        @NotNull
        @DecimalMin("0")
        BigDecimal fatPer100g,

        @NotNull
        @DecimalMin("0")
        BigDecimal sugarPer100g,

        @NotNull
        @DecimalMin("0")
        BigDecimal fiberPer100g
) {
}