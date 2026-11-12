package com.lifekhmer.appointment.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentResponse(
        UUID id,
        String doctorName,
        String healthcareFacility,
        LocalDateTime appointmentTime,
        String purpose,
        String status,
        String notes,
        int reminderMinutesBefore,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
