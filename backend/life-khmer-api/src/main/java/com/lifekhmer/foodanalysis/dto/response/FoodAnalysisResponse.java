package com.lifekhmer.foodanalysis.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record FoodAnalysisResponse(
        UUID id,
        String imageUrl,
        String detectedFoodName,
        BigDecimal confidence,
        String status,
        BigDecimal estimatedCalories,
        BigDecimal estimatedProtein,
        BigDecimal estimatedCarbohydrates,
        BigDecimal estimatedFat,
        BigDecimal suggestedPortionGrams,
        boolean isEstimate,
        String disclaimer,
        LocalDateTime createdAt
) {}
