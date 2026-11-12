package com.lifekhmer.dashboard.dto.response;

import com.lifekhmer.appointment.dto.response.AppointmentResponse;
import com.lifekhmer.health.dto.response.MeasurementResponse;
import com.lifekhmer.medication.dto.response.MedicationResponse;
import com.lifekhmer.nutrition.dto.response.NutritionSummaryResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record DashboardSummaryResponse(
        UUID userId,
        String fullName,
        String preferredLanguage,
        BigDecimal heightCm,
        BigDecimal weightKg,
        NutritionSummaryResponse dailyNutrition,
        BigDecimal targetCalories,
        Map<String, MeasurementResponse> latestMeasurements,
        List<MedicationResponse> activeMedications,
        List<AppointmentResponse> upcomingAppointments,
        long totalAppointmentsCount,
        long totalMedicationsCount
) {}
