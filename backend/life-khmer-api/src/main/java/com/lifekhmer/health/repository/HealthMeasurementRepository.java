package com.lifekhmer.health.repository;

import com.lifekhmer.health.entity.HealthMeasurement;
import com.lifekhmer.health.entity.MeasurementType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HealthMeasurementRepository extends JpaRepository<HealthMeasurement, UUID> {
    Optional<HealthMeasurement> findByIdAndUserId(UUID id, UUID userId);

    Page<HealthMeasurement> findByUserIdAndRecordedAtBetween(
            UUID userId,
            LocalDateTime from,
            LocalDateTime to,
            Pageable pageable
    );

    Page<HealthMeasurement> findByUserIdAndMeasurementTypeAndRecordedAtBetween(
            UUID userId,
            MeasurementType type,
            LocalDateTime from,
            LocalDateTime to,
            Pageable pageable
    );

    Optional<HealthMeasurement> findFirstByUserIdAndMeasurementTypeOrderByRecordedAtDesc(
            UUID userId,
            MeasurementType type
    );
}
