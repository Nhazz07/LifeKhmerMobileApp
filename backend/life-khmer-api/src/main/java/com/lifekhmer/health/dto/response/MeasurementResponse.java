package com.lifekhmer.health.dto.response;

import com.lifekhmer.health.entity.MeasurementType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record MeasurementResponse(
        UUID id,
        MeasurementType measurementType,
        BigDecimal value,
        BigDecimal secondaryValue,
        String unit,
        LocalDateTime recordedAt,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}