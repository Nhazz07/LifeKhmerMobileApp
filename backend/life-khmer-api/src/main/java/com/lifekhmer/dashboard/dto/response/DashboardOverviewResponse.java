package com.lifekhmer.dashboard.dto.response;

import com.lifekhmer.health.entity.MeasurementType;
import com.lifekhmer.nutrition.entity.MealType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record DashboardOverviewResponse(
        String greeting,
        String userName,
        ProfileSummary profile,
        NutritionTodaySummary nutritionToday,
        HealthSnapshot healthSnapshot,
        List<RecentMealSummary> recentMeals,
        List<RecentMeasurementSummary> recentMeasurements,
        List<UpcomingAppointmentSummary> upcomingAppointments,
        List<ActiveMedicationSummary> activeMedications,
        LocalDateTime generatedAt
) {

    public record ProfileSummary(
            UUID id,
            String fullName,
            String gender,
            BigDecimal heightCm,
            BigDecimal weightKg,
            String preferredLanguage
    ) {}

    public record NutritionTodaySummary(
            LocalDate date,
            BigDecimal calories,
            BigDecimal protein,
            BigDecimal carbohydrates,
            BigDecimal fat,
            BigDecimal sugar,
            BigDecimal fiber
    ) {}

    public record HealthSnapshot(
            BigDecimal latestWeightKg,
            BigDecimal latestHeightCm,
            BigDecimal latestBloodPressureSystolic,
            BigDecimal latestBloodPressureDiastolic,
            BigDecimal latestHeartRate,
            LocalDateTime latestMeasurementDate
    ) {}

    public record RecentMealSummary(
            UUID id,
            String foodName,
            MealType mealType,
            BigDecimal calories,
            LocalDateTime consumedAt
    ) {}

    public record RecentMeasurementSummary(
            UUID id,
            MeasurementType measurementType,
            BigDecimal value,
            BigDecimal secondaryValue,
            String unit,
            LocalDateTime recordedAt
    ) {}

    public record UpcomingAppointmentSummary(
            UUID id,
            String doctorName,
            String healthcareFacility,
            LocalDateTime appointmentTime,
            String purpose,
            String status
    ) {}

    public record ActiveMedicationSummary(
            UUID id,
            String name,
            String dosage,
            String frequency
    ) {}
}
