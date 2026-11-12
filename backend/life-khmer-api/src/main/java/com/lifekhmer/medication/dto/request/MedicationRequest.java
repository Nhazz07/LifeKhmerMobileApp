package com.lifekhmer.medication.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record MedicationRequest(
        @NotBlank(message = "Medication name is required")
        @Size(max = 150)
        String name,

        @NotBlank(message = "Dosage is required")
        @Size(max = 100)
        String dosage,

        @NotBlank(message = "Frequency is required")
        @Size(max = 100)
        String frequency,

        @Size(max = 500)
        String instructions,

        @NotNull(message = "Start date is required")
        LocalDate startDate,

        LocalDate endDate,

        Boolean active
) {}
