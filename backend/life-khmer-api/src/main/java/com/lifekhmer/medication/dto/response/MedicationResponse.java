package com.lifekhmer.medication.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record MedicationResponse(
        UUID id,
        String name,
        String dosage,
        String frequency,
        String instructions,
        LocalDate startDate,
        LocalDate endDate,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
