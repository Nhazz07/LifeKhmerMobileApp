package com.lifekhmer.nutrition.repository; import com.lifekhmer.nutrition.entity.Food; import org.springframework.data.domain.*; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface FoodRepository extends JpaRepository<Food,UUID> {
    boolean existsByNormalizedName(String normalizedName);
    Page<Food> findByNormalizedNameContaining(String query, Pageable pageable); }
