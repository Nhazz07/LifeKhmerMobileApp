package com.lifekhmer.medication.repository;

import com.lifekhmer.medication.entity.MedicationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MedicationLogRepository extends JpaRepository<MedicationLog, UUID> {
    Page<MedicationLog> findByUserId(UUID userId, Pageable pageable);
    List<MedicationLog> findByUserIdAndScheduledTimeBetween(UUID userId, LocalDateTime start, LocalDateTime end);
    Optional<MedicationLog> findByIdAndUserId(UUID id, UUID userId);
}
