package com.lifekhmer.health.dto.request;

import com.lifekhmer.health.entity.MeasurementType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MeasurementRequest(
        @NotNull
        MeasurementType measurementType,

        @NotNull
        @DecimalMin("0.01")
        @DecimalMax("1000")
        BigDecimal value,

        @DecimalMin("0.01")
        @DecimalMax("1000")
        BigDecimal secondaryValue,

        @NotBlank
        @Size(max = 20)
        String unit,

        @NotNull
        @PastOrPresent
        LocalDateTime recordedAt,

        @Size(max = 1000)
        String notes
) {
}