package com.lifekhmer.accounts.dto.response;
import java.math.BigDecimal; import java.time.*; import java.util.UUID;
public record ProfileResponse(UUID id, String fullName, LocalDate dateOfBirth, String gender, BigDecimal heightCm, BigDecimal weightKg, String preferredLanguage, String profileImageKey, String status, LocalDateTime createdAt, LocalDateTime updatedAt) { }
