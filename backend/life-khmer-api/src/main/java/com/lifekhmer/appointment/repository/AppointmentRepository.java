package com.lifekhmer.appointment.repository;

import com.lifekhmer.appointment.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    Page<Appointment> findByUserId(UUID userId, Pageable pageable);
    Optional<Appointment> findByIdAndUserId(UUID id, UUID userId);
    List<Appointment> findByUserIdAndAppointmentTimeAfterAndStatusOrderByAppointmentTimeAsc(
            UUID userId,
            LocalDateTime after,
            String status
    );
    long countByUserIdAndAppointmentTimeAfterAndStatus(UUID userId, LocalDateTime after, String status);
}
