package com.lifekhmer.authentication.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record RegisterResponse(
        UUID userId,
        UUID accountId,
        String fullName,
        String email,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String message
) {
}
