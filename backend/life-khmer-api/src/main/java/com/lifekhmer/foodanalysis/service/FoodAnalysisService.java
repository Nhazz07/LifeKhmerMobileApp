package com.lifekhmer.foodanalysis.service;

import com.lifekhmer.foodanalysis.dto.request.ConfirmFoodAnalysisRequest;
import com.lifekhmer.foodanalysis.dto.response.FoodAnalysisResponse;
import com.lifekhmer.nutrition.dto.response.MealResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface FoodAnalysisService {
    FoodAnalysisResponse analyzeFoodImage(MultipartFile file);
    FoodAnalysisResponse getAnalysis(UUID id);
    Page<FoodAnalysisResponse> listAnalyses(int page, int size);
    MealResponse confirmAndSaveMeal(UUID id, ConfirmFoodAnalysisRequest request);
}
