package com.lifekhmer.accounts.dto.request;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
public record UpdateProfileRequest(@NotBlank @Size(max=100) String fullName, @Past LocalDate dateOfBirth, @Size(max=30) String gender, @DecimalMin("30.00") @DecimalMax("300.00") BigDecimal heightCm, @DecimalMin("2.00") @DecimalMax("500.00") BigDecimal weightKg, @NotBlank @Size(max=10) String preferredLanguage) { }
