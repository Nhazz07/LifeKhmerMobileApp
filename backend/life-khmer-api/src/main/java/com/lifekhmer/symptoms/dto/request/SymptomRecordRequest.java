package com.lifekhmer.symptoms.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record SymptomRecordRequest(
        @NotBlank(message = "Symptom name is required")
        @Size(max = 150)
        String symptomName,

        @NotNull(message = "Severity is required")
        @Min(value = 1, message = "Severity must be between 1 and 10")
        @Max(value = 10, message = "Severity must be between 1 and 10")
        Integer severity,

        @Size(max = 1000)
        String notes,

        @NotNull(message = "Recorded at time is required")
        LocalDateTime recordedAt
) {}
