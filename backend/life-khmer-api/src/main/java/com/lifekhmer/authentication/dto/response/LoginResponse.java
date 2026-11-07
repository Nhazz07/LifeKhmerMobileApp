package com.lifekhmer.authentication.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record LoginResponse(
        UUID userId,
        UUID accountId,
        String fullName,
        String email,
        String accessToken,
        String refreshToken,
        String tokenType,
        Long expiresIn,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
