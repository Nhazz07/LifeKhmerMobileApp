package com.lifekhmer.nutrition.service;

import com.lifekhmer.nutrition.dto.request.FoodRequest;
import com.lifekhmer.nutrition.dto.request.MealRequest;
import com.lifekhmer.nutrition.dto.response.FoodResponse;
import com.lifekhmer.nutrition.dto.response.MealResponse;
import com.lifekhmer.nutrition.dto.response.NutritionSummaryResponse;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.UUID;

public interface NutritionService {

    FoodResponse createFood(FoodRequest request);

    Page<FoodResponse> foods(String query, int page, int size);

    FoodResponse getFood(UUID id);

    MealResponse createMeal(MealRequest request);

    MealResponse updateMeal(UUID id, MealRequest request);

    void deleteMeal(UUID id);

    Page<MealResponse> meals(LocalDate from, LocalDate to, int page, int size);

    NutritionSummaryResponse dailySummary(LocalDate date);
}
