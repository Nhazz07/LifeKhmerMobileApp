package com.lifekhmer.nutrition.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record FoodResponse(
        UUID id,
        String name,
        BigDecimal caloriesPer100g,
        BigDecimal proteinPer100g,
        BigDecimal carbohydratesPer100g,
        BigDecimal fatPer100g,
        BigDecimal sugarPer100g,
        BigDecimal fiberPer100g
) {
}