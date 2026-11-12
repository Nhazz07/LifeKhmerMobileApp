package com.lifekhmer.dashboard.controller;

import com.lifekhmer.common.response.ApiResponse;
import com.lifekhmer.dashboard.dto.response.DashboardOverviewResponse;
import com.lifekhmer.dashboard.dto.response.GuestDashboardInfo;
import com.lifekhmer.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/overview")
    public ResponseEntity<ApiResponse<DashboardOverviewResponse>> getOverview() {
        DashboardOverviewResponse overview = dashboardService.getOverview();
        return ResponseEntity.ok(new ApiResponse<>(true, "Dashboard overview retrieved", overview));
    }

    @GetMapping("/guest-info")
    public ResponseEntity<ApiResponse<GuestDashboardInfo>> getGuestInfo() {
        GuestDashboardInfo info = new GuestDashboardInfo(
                "LifeKhmer",
                "Smart Health & Nutrition for Cambodia",
                List.of(
                        "Personal Health Tracking",
                        "Daily Nutrition Logging",
                        "AI Food Scanner",
                        "Medication Reminders",
                        "Appointment Management",
                        "Symptom Diary"
                ),
                "Join thousands of Cambodians taking control of their health"
        );
        return ResponseEntity.ok(new ApiResponse<>(true, "Guest info retrieved", info));
    }
}
