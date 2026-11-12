package com.lifekhmer.symptoms.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record SymptomRecordResponse(
        UUID id,
        String symptomName,
        int severity,
        String notes,
        LocalDateTime recordedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
