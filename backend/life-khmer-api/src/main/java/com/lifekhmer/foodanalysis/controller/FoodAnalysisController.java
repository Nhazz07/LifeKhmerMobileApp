package com.lifekhmer.foodanalysis.controller;

import com.lifekhmer.common.response.ApiResponse;
import com.lifekhmer.foodanalysis.dto.request.ConfirmFoodAnalysisRequest;
import com.lifekhmer.foodanalysis.dto.response.FoodAnalysisResponse;
import com.lifekhmer.foodanalysis.service.FoodAnalysisService;
import com.lifekhmer.nutrition.dto.response.MealResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/food-analysis")
@RequiredArgsConstructor
public class FoodAnalysisController {

    private final FoodAnalysisService foodAnalysisService;

    @PostMapping(value = "/analyze", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<FoodAnalysisResponse>> analyze(@RequestParam("image") MultipartFile image) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Image analyzed successfully", foodAnalysisService.analyzeFoodImage(image)));
    }

    @GetMapping("/{id}")
    public ApiResponse<FoodAnalysisResponse> get(@PathVariable UUID id) {
        return new ApiResponse<>(true, "Analysis retrieved", foodAnalysisService.getAnalysis(id));
    }

    @GetMapping
    public ApiResponse<Page<FoodAnalysisResponse>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return new ApiResponse<>(true, "Food analyses retrieved", foodAnalysisService.listAnalyses(page, size));
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<ApiResponse<MealResponse>> confirm(
            @PathVariable UUID id,
            @Valid @RequestBody ConfirmFoodAnalysisRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Meal recorded from food analysis", foodAnalysisService.confirmAndSaveMeal(id, request)));
    }
}
