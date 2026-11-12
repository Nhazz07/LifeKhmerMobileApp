package com.lifekhmer.appointment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record AppointmentRequest(
        @NotBlank(message = "Doctor name is required")
        @Size(max = 150)
        String doctorName,

        @NotBlank(message = "Healthcare facility is required")
        @Size(max = 200)
        String healthcareFacility,

        @NotNull(message = "Appointment time is required")
        LocalDateTime appointmentTime,

        @NotBlank(message = "Purpose is required")
        @Size(max = 255)
        String purpose,

        @Size(max = 30)
        String status, // SCHEDULED, COMPLETED, CANCELLED

        @Size(max = 1000)
        String notes,

        Integer reminderMinutesBefore
) {}
