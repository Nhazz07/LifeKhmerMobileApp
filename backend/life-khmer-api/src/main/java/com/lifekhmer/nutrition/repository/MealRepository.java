package com.lifekhmer.nutrition.repository; import com.lifekhmer.nutrition.entity.Meal; import org.springframework.data.domain.*; import org.springframework.data.jpa.repository.JpaRepository; import java.time.*; import java.util.*;
public interface MealRepository extends JpaRepository<Meal,UUID> {
    Optional<Meal> findByIdAndUserId(UUID id,UUID userId);
    Page<Meal> findByUserIdAndConsumedAtBetween(UUID userId,LocalDateTime from,LocalDateTime to,Pageable pageable); }
