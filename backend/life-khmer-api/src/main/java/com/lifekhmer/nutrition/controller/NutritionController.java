package com.lifekhmer.nutrition.controller;

import com.lifekhmer.common.response.ApiResponse;
import com.lifekhmer.nutrition.dto.request.FoodRequest;
import com.lifekhmer.nutrition.dto.request.MealRequest;
import com.lifekhmer.nutrition.dto.response.FoodResponse;
import com.lifekhmer.nutrition.dto.response.MealResponse;
import com.lifekhmer.nutrition.dto.response.NutritionSummaryResponse;
import com.lifekhmer.nutrition.service.NutritionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/nutrition")
@RequiredArgsConstructor
public class NutritionController {

    private final NutritionService service;

    @PostMapping("/foods")
    public ResponseEntity<ApiResponse<FoodResponse>> createFood(@Valid @RequestBody FoodRequest r) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Food created", service.createFood(r)));
    }

    @GetMapping("/foods")
    public ApiResponse<Page<FoodResponse>> foods(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return new ApiResponse<>(true, "Foods retrieved", service.foods(query, page, size));
    }

    @GetMapping("/foods/{id}")
    public ApiResponse<FoodResponse> getFood(@PathVariable UUID id) {
        return new ApiResponse<>(true, "Food details retrieved", service.getFood(id));
    }

    @PostMapping("/meals")
    public ResponseEntity<ApiResponse<MealResponse>> createMeal(@Valid @RequestBody MealRequest r) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Meal recorded", service.createMeal(r)));
    }

    @GetMapping("/meals")
    public ApiResponse<Page<MealResponse>> meals(
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return new ApiResponse<>(true, "Meals retrieved", service.meals(from, to, page, size));
    }

    @PutMapping("/meals/{id}")
    public ApiResponse<MealResponse> updateMeal(
            @PathVariable UUID id,
            @Valid @RequestBody MealRequest r) {
        return new ApiResponse<>(true, "Meal updated", service.updateMeal(id, r));
    }

    @DeleteMapping("/meals/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMeal(@PathVariable UUID id) {
        service.deleteMeal(id);
    }

    @GetMapping("/summary")
    public ApiResponse<NutritionSummaryResponse> summary(@RequestParam(required = false) LocalDate date) {
        return new ApiResponse<>(true, "Nutrition summary retrieved", service.dailySummary(date));
    }
}
