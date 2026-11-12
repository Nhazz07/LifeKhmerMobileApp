package com.lifekhmer.dashboard.service;

import com.lifekhmer.accounts.entity.User;
import com.lifekhmer.accounts.repository.UserRepository;
import com.lifekhmer.appointment.repository.AppointmentRepository;
import com.lifekhmer.dashboard.dto.response.DashboardOverviewResponse;
import com.lifekhmer.dashboard.service.impl.DashboardServiceImpl;
import com.lifekhmer.health.entity.MeasurementType;
import com.lifekhmer.health.repository.HealthMeasurementRepository;
import com.lifekhmer.medication.repository.MedicationRepository;
import com.lifekhmer.nutrition.repository.MealRepository;
import com.lifekhmer.security.CurrentUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    private CurrentUserService currentUserService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MealRepository mealRepository;

    @Mock
    private HealthMeasurementRepository healthMeasurementRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private MedicationRepository medicationRepository;

    @InjectMocks
    private DashboardServiceImpl dashboardService;

    @Test
    void getOverview_returnsCorrectGreetingAndEmptyData() {
        // Setup
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setFullName("Dara Test");

        when(currentUserService.getCurrentUserId()).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Mock meals - today's nutrition and recent meals
        when(mealRepository.findByUserIdAndConsumedAtBetween(
                eq(userId), any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(Page.empty());

        // Mock health measurements
        when(healthMeasurementRepository.findFirstByUserIdAndMeasurementTypeOrderByRecordedAtDesc(
                eq(userId), any(MeasurementType.class)))
                .thenReturn(Optional.empty());

        // Mock recent measurements
        when(healthMeasurementRepository.findByUserIdAndRecordedAtBetween(
                eq(userId), any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(Page.empty());

        // Mock appointments
        when(appointmentRepository.findByUserIdAndAppointmentTimeAfterAndStatusOrderByAppointmentTimeAsc(
                eq(userId), any(LocalDateTime.class), eq("SCHEDULED")))
                .thenReturn(List.of());

        // Mock medications
        when(medicationRepository.findByUserIdAndActiveTrue(userId)).thenReturn(List.of());

        // Execute
        DashboardOverviewResponse response = dashboardService.getOverview();

        // Assert
        assertThat(response.userName()).isEqualTo("Dara Test");
        assertThat(response.nutritionToday().calories()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(response.recentMeals()).isEmpty();
        assertThat(response.recentMeasurements()).isEmpty();
        assertThat(response.upcomingAppointments()).isEmpty();
    }
}
