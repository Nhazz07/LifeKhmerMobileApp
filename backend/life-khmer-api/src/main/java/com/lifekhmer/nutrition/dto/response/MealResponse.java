package com.lifekhmer.nutrition.dto.response; import com.lifekhmer.nutrition.entity.MealType; import java.math.BigDecimal; import java.time.LocalDateTime; import java.util.UUID;
public record MealResponse(
        UUID id,
        MealType mealType,
        String foodName,
        BigDecimal portionGrams,
        BigDecimal calories,
        BigDecimal protein,
        BigDecimal carbohydrates,
        BigDecimal fat,
        BigDecimal sugar,
        BigDecimal fiber,
        LocalDateTime consumedAt,
        String notes) {

}
