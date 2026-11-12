package com.lifekhmer.medication.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public record MedicationLogRequest(
        @NotNull(message = "Medication ID is required")
        UUID medicationId,

        @NotNull(message = "Scheduled time is required")
        LocalDateTime scheduledTime,

        LocalDateTime takenAt,

        @Size(max = 30)
        String status, // TAKEN, MISSED, SKIPPED

        @Size(max = 500)
        String notes
) {}
