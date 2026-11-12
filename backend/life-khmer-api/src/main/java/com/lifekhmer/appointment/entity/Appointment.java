package com.lifekhmer.appointment.entity;

import com.lifekhmer.accounts.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "doctor_name", nullable = false, length = 150)
    private String doctorName;

    @Column(name = "healthcare_facility", nullable = false, length = 200)
    private String healthcareFacility;

    @Column(name = "appointment_time", nullable = false)
    private LocalDateTime appointmentTime;

    @Column(nullable = false, length = 255)
    private String purpose;

    @Column(nullable = false, length = 30)
    private String status = "SCHEDULED"; // SCHEDULED, COMPLETED, CANCELLED

    @Column(length = 1000)
    private String notes;

    @Column(name = "reminder_minutes_before", nullable = false)
    private int reminderMinutesBefore = 60;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
