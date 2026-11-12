package com.lifekhmer.dashboard.service.impl;

import com.lifekhmer.accounts.entity.User;
import com.lifekhmer.accounts.repository.UserRepository;
import com.lifekhmer.appointment.repository.AppointmentRepository;
import com.lifekhmer.dashboard.dto.response.DashboardOverviewResponse;
import com.lifekhmer.dashboard.service.DashboardService;
import com.lifekhmer.health.entity.MeasurementType;
import com.lifekhmer.health.repository.HealthMeasurementRepository;
import com.lifekhmer.medication.repository.MedicationRepository;
import com.lifekhmer.nutrition.repository.MealRepository;
import com.lifekhmer.security.CurrentUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardServiceImpl implements DashboardService {

    private final CurrentUserService currentUserService;
    private final UserRepository userRepository;
    private final MealRepository mealRepository;
    private final HealthMeasurementRepository healthMeasurementRepository;
    private final AppointmentRepository appointmentRepository;
    private final MedicationRepository medicationRepository;

    @Override
    @Transactional(readOnly = true)
    public DashboardOverviewResponse getOverview() {
        UUID userId = currentUserService.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        String greeting = buildGreeting(user);
        String userName = user.getFullName();

        DashboardOverviewResponse.ProfileSummary profile = new DashboardOverviewResponse.ProfileSummary(
                user.getId(),
                user.getFullName(),
                user.getGender(),
                user.getHeightCm(),
                user.getWeightKg(),
                user.getPreferredLanguage()
        );

        DashboardOverviewResponse.NutritionTodaySummary nutritionToday = buildNutritionToday(userId);
        DashboardOverviewResponse.HealthSnapshot healthSnapshot = buildHealthSnapshot(userId);
        List<DashboardOverviewResponse.RecentMealSummary> recentMeals = buildRecentMeals(userId);
        List<DashboardOverviewResponse.RecentMeasurementSummary> recentMeasurements = buildRecentMeasurements(userId);
        List<DashboardOverviewResponse.UpcomingAppointmentSummary> upcomingAppointments = buildUpcomingAppointments(userId);
        List<DashboardOverviewResponse.ActiveMedicationSummary> activeMedications = buildActiveMedications(userId);

        return new DashboardOverviewResponse(
                greeting,
                userName,
                profile,
                nutritionToday,
                healthSnapshot,
                recentMeals,
                recentMeasurements,
                upcomingAppointments,
                activeMedications,
                LocalDateTime.now()
        );
    }

    private String buildGreeting(User user) {
        int hour = LocalDateTime.now().getHour();
        String timeOfDay;
        if (hour < 12) {
            timeOfDay = "Good morning";
        } else if (hour < 18) {
            timeOfDay = "Good afternoon";
        } else {
            timeOfDay = "Good evening";
        }
        String firstName = user.getFullName() != null
                ? user.getFullName().split("\\s+")[0]
                : "there";
        return timeOfDay + ", " + firstName + "!";
    }

    private DashboardOverviewResponse.NutritionTodaySummary buildNutritionToday(UUID userId) {
        try {
            LocalDate today = LocalDate.now();
            LocalDateTime start = today.atStartOfDay();
            LocalDateTime end = today.plusDays(1).atStartOfDay();

            var meals = mealRepository.findByUserIdAndConsumedAtBetween(userId, start, end, Pageable.unpaged())
                    .getContent();

            BigDecimal calories = meals.stream().map(m -> m.getCalories()).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal protein = meals.stream().map(m -> m.getProtein()).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal carbohydrates = meals.stream().map(m -> m.getCarbohydrates()).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal fat = meals.stream().map(m -> m.getFat()).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal sugar = meals.stream().map(m -> m.getSugar()).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal fiber = meals.stream().map(m -> m.getFiber()).reduce(BigDecimal.ZERO, BigDecimal::add);

            return new DashboardOverviewResponse.NutritionTodaySummary(today, calories, protein, carbohydrates, fat, sugar, fiber);
        } catch (Exception e) {
            log.error("Failed to build nutrition today summary for user {}: {}", userId, e.getMessage());
            return new DashboardOverviewResponse.NutritionTodaySummary(
                    LocalDate.now(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                    BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        }
    }

    private DashboardOverviewResponse.HealthSnapshot buildHealthSnapshot(UUID userId) {
        try {
            var weightOpt = healthMeasurementRepository.findFirstByUserIdAndMeasurementTypeOrderByRecordedAtDesc(userId, MeasurementType.WEIGHT);
            var heightOpt = healthMeasurementRepository.findFirstByUserIdAndMeasurementTypeOrderByRecordedAtDesc(userId, MeasurementType.HEIGHT);
            var heartRateOpt = healthMeasurementRepository.findFirstByUserIdAndMeasurementTypeOrderByRecordedAtDesc(userId, MeasurementType.HEART_RATE);
            var bpOpt = healthMeasurementRepository.findFirstByUserIdAndMeasurementTypeOrderByRecordedAtDesc(userId, MeasurementType.BLOOD_PRESSURE);

            BigDecimal weightKg = weightOpt.map(m -> m.getValue()).orElse(null);
            BigDecimal heightCm = heightOpt.map(m -> m.getValue()).orElse(null);
            BigDecimal heartRate = heartRateOpt.map(m -> m.getValue()).orElse(null);
            BigDecimal bpSystolic = bpOpt.map(m -> m.getValue()).orElse(null);
            BigDecimal bpDiastolic = bpOpt.map(m -> m.getSecondaryValue()).orElse(null);

            LocalDateTime latestDate = Stream.of(
                    weightOpt.map(m -> m.getRecordedAt()).orElse(null),
                    heightOpt.map(m -> m.getRecordedAt()).orElse(null),
                    heartRateOpt.map(m -> m.getRecordedAt()).orElse(null),
                    bpOpt.map(m -> m.getRecordedAt()).orElse(null)
            )
                    .filter(d -> d != null)
                    .max(LocalDateTime::compareTo)
                    .orElse(null);

            return new DashboardOverviewResponse.HealthSnapshot(weightKg, heightCm, bpSystolic, bpDiastolic, heartRate, latestDate);
        } catch (Exception e) {
            log.error("Failed to build health snapshot for user {}: {}", userId, e.getMessage());
            return new DashboardOverviewResponse.HealthSnapshot(null, null, null, null, null, null);
        }
    }

    private List<DashboardOverviewResponse.RecentMealSummary> buildRecentMeals(UUID userId) {
        try {
            LocalDateTime to = LocalDateTime.now();
            LocalDateTime from = to.minusDays(7);
            Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "consumedAt"));

            return mealRepository.findByUserIdAndConsumedAtBetween(userId, from, to, pageable)
                    .getContent()
                    .stream()
                    .map(m -> new DashboardOverviewResponse.RecentMealSummary(
                            m.getId(),
                            m.getFoodName(),
                            m.getMealType(),
                            m.getCalories(),
                            m.getConsumedAt()
                    ))
                    .toList();
        } catch (Exception e) {
            log.error("Failed to build recent meals for user {}: {}", userId, e.getMessage());
            return Collections.emptyList();
        }
    }

    private List<DashboardOverviewResponse.RecentMeasurementSummary> buildRecentMeasurements(UUID userId) {
        try {
            LocalDateTime to = LocalDateTime.now();
            LocalDateTime from = to.minusDays(30);
            Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "recordedAt"));

            return healthMeasurementRepository.findByUserIdAndRecordedAtBetween(userId, from, to, pageable)
                    .getContent()
                    .stream()
                    .map(m -> new DashboardOverviewResponse.RecentMeasurementSummary(
                            m.getId(),
                            m.getMeasurementType(),
                            m.getValue(),
                            m.getSecondaryValue(),
                            m.getUnit(),
                            m.getRecordedAt()
                    ))
                    .toList();
        } catch (Exception e) {
            log.error("Failed to build recent measurements for user {}: {}", userId, e.getMessage());
            return Collections.emptyList();
        }
    }

    private List<DashboardOverviewResponse.UpcomingAppointmentSummary> buildUpcomingAppointments(UUID userId) {
        try {
            return appointmentRepository
                    .findByUserIdAndAppointmentTimeAfterAndStatusOrderByAppointmentTimeAsc(userId, LocalDateTime.now(), "SCHEDULED")
                    .stream()
                    .limit(3)
                    .map(a -> new DashboardOverviewResponse.UpcomingAppointmentSummary(
                            a.getId(),
                            a.getDoctorName(),
                            a.getHealthcareFacility(),
                            a.getAppointmentTime(),
                            a.getPurpose(),
                            a.getStatus()
                    ))
                    .toList();
        } catch (Exception e) {
            log.error("Failed to build upcoming appointments for user {}: {}", userId, e.getMessage());
            return Collections.emptyList();
        }
    }

    private List<DashboardOverviewResponse.ActiveMedicationSummary> buildActiveMedications(UUID userId) {
        try {
            return medicationRepository.findByUserIdAndActiveTrue(userId)
                    .stream()
                    .limit(5)
                    .map(m -> new DashboardOverviewResponse.ActiveMedicationSummary(
                            m.getId(),
                            m.getName(),
                            m.getDosage(),
                            m.getFrequency()
                    ))
                    .toList();
        } catch (Exception e) {
            log.error("Failed to build active medications for user {}: {}", userId, e.getMessage());
            return Collections.emptyList();
        }
    }
}
