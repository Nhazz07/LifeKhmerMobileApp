package com.lifekhmer.symptoms.repository;

import com.lifekhmer.symptoms.entity.SymptomRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SymptomRecordRepository extends JpaRepository<SymptomRecord, UUID> {
    Page<SymptomRecord> findByUserId(UUID userId, Pageable pageable);
    Optional<SymptomRecord> findByIdAndUserId(UUID id, UUID userId);
}
