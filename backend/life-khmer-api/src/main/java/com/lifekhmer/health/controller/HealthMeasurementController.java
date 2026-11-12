package com.lifekhmer.health.controller;

import com.lifekhmer.common.response.ApiResponse;
import com.lifekhmer.health.dto.request.MeasurementRequest;
import com.lifekhmer.health.dto.response.MeasurementResponse;
import com.lifekhmer.health.entity.MeasurementType;
import com.lifekhmer.health.service.HealthMeasurementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/health/measurements")
@RequiredArgsConstructor
public class HealthMeasurementController {

    private final HealthMeasurementService service;

    @PostMapping
    public ResponseEntity<ApiResponse<MeasurementResponse>> create(
            @Valid @RequestBody MeasurementRequest r) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Measurement recorded", service.create(r)));
    }

    @GetMapping
    public ApiResponse<Page<MeasurementResponse>> list(
            @RequestParam(required = false) MeasurementType type,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return new ApiResponse<>(true, "Measurements retrieved",
                service.list(type, from, to, page, size));
    }

    @GetMapping("/{id}")
    public ApiResponse<MeasurementResponse> get(@PathVariable UUID id) {
        return new ApiResponse<>(true, "Measurement retrieved", service.get(id));
    }

    @PutMapping("/{id}")
    public ApiResponse<MeasurementResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody MeasurementRequest r) {
        return new ApiResponse<>(true, "Measurement updated", service.update(id, r));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}