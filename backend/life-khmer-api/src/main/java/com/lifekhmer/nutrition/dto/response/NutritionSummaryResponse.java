package com.lifekhmer.nutrition.dto.response; import java.math.BigDecimal; import java.time.LocalDate;
public record NutritionSummaryResponse(
        LocalDate date,
        BigDecimal calories,
        BigDecimal protein,
        BigDecimal carbohydrates,
        BigDecimal fat,
        BigDecimal sugar,
        BigDecimal fiber) {

}
