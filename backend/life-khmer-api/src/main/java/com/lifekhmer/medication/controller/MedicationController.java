package com.lifekhmer.medication.controller;

import com.lifekhmer.common.response.ApiResponse;
import com.lifekhmer.medication.dto.request.MedicationLogRequest;
import com.lifekhmer.medication.dto.request.MedicationRequest;
import com.lifekhmer.medication.dto.response.MedicationLogResponse;
import com.lifekhmer.medication.dto.response.MedicationResponse;
import com.lifekhmer.medication.service.MedicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/medications")
@RequiredArgsConstructor
public class MedicationController {

    private final MedicationService medicationService;

    @PostMapping
    public ResponseEntity<ApiResponse<MedicationResponse>> create(@Valid @RequestBody MedicationRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Medication created successfully", medicationService.createMedication(request)));
    }

    @GetMapping
    public ApiResponse<Page<MedicationResponse>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return new ApiResponse<>(true, "Medications retrieved", medicationService.listMedications(page, size));
    }

    @GetMapping("/active")
    public ApiResponse<List<MedicationResponse>> listActive() {
        return new ApiResponse<>(true, "Active medications retrieved", medicationService.listActiveMedications());
    }

    @GetMapping("/{id}")
    public ApiResponse<MedicationResponse> get(@PathVariable UUID id) {
        return new ApiResponse<>(true, "Medication retrieved", medicationService.getMedication(id));
    }

    @PutMapping("/{id}")
    public ApiResponse<MedicationResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody MedicationRequest request) {
        return new ApiResponse<>(true, "Medication updated", medicationService.updateMedication(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        medicationService.deleteMedication(id);
    }

    @PostMapping("/logs")
    public ResponseEntity<ApiResponse<MedicationLogResponse>> recordLog(@Valid @RequestBody MedicationLogRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Medication log recorded", medicationService.recordLog(request)));
    }

    @GetMapping("/logs")
    public ApiResponse<Page<MedicationLogResponse>> listLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return new ApiResponse<>(true, "Medication logs retrieved", medicationService.listLogs(page, size));
    }
}
