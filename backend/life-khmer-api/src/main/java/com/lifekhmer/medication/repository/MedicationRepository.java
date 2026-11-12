package com.lifekhmer.medication.repository;

import com.lifekhmer.medication.entity.Medication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, UUID> {
    Page<Medication> findByUserId(UUID userId, Pageable pageable);
    List<Medication> findByUserIdAndActiveTrue(UUID userId);
    Optional<Medication> findByIdAndUserId(UUID id, UUID userId);
    long countByUserIdAndActiveTrue(UUID userId);
}
