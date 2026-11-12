package com.lifekhmer.foodanalysis.repository;

import com.lifekhmer.foodanalysis.entity.FoodAnalysis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FoodAnalysisRepository extends JpaRepository<FoodAnalysis, UUID> {
    Page<FoodAnalysis> findByUserId(UUID userId, Pageable pageable);
    Optional<FoodAnalysis> findByIdAndUserId(UUID id, UUID userId);
}
