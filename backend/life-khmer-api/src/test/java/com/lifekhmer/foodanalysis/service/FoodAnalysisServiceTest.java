package com.lifekhmer.foodanalysis.service;

import com.lifekhmer.accounts.entity.User;
import com.lifekhmer.accounts.repository.UserRepository;
import com.lifekhmer.foodanalysis.dto.response.FoodAnalysisResponse;
import com.lifekhmer.foodanalysis.entity.FoodAnalysis;
import com.lifekhmer.foodanalysis.repository.FoodAnalysisRepository;
import com.lifekhmer.foodanalysis.service.impl.FoodAnalysisServiceImpl;
import com.lifekhmer.nutrition.repository.FoodRepository;
import com.lifekhmer.nutrition.repository.MealRepository;
import com.lifekhmer.security.CurrentUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FoodAnalysisServiceTest {

    @Mock
    private FoodAnalysisRepository foodAnalysisRepository;
    @Mock
    private FoodRepository foodRepository;
    @Mock
    private MealRepository mealRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private FoodAnalysisServiceImpl foodAnalysisService;

    private User user;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = new User();
        user.setId(userId);
        user.setFullName("Chann Sok");
    }

    @Test
    void testAnalyzeFoodImageSuccess() {
        when(currentUserService.getCurrentUserId()).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        FoodAnalysis saved = new FoodAnalysis();
        saved.setId(UUID.randomUUID());
        saved.setUser(user);
        saved.setDetectedFoodName("Fish Amok (Amok Trey)");
        saved.setConfidence(BigDecimal.valueOf(94.5));
        saved.setEstimatedCalories(BigDecimal.valueOf(362.5));
        saved.setEstimatedProtein(BigDecimal.valueOf(30.0));
        saved.setEstimatedCarbohydrates(BigDecimal.valueOf(11.25));
        saved.setEstimatedFat(BigDecimal.valueOf(22.5));
        saved.setSuggestedPortionGrams(BigDecimal.valueOf(250));

        when(foodAnalysisRepository.save(any(FoodAnalysis.class))).thenReturn(saved);

        MockMultipartFile file = new MockMultipartFile(
                "image",
                "fish_amok.jpg",
                "image/jpeg",
                new byte[]{1, 2, 3, 4}
        );

        FoodAnalysisResponse response = foodAnalysisService.analyzeFoodImage(file);

        assertThat(response).isNotNull();
        assertThat(response.detectedFoodName()).isEqualTo("Fish Amok (Amok Trey)");
        assertThat(response.isEstimate()).isTrue();
        assertThat(response.disclaimer()).contains("guidance");
        verify(foodAnalysisRepository).save(any(FoodAnalysis.class));
    }
}
