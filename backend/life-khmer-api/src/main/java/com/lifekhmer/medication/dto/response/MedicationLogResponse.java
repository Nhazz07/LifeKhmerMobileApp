package com.lifekhmer.medication.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record MedicationLogResponse(
        UUID id,
        UUID medicationId,
        String medicationName,
        LocalDateTime scheduledTime,
        LocalDateTime takenAt,
        String status,
        String notes,
        LocalDateTime createdAt
) {}
