package com.lifekhmer.nutrition.service.impl;

import com.lifekhmer.accounts.entity.User;
import com.lifekhmer.accounts.repository.UserRepository;
import com.lifekhmer.common.exception.DuplicateResourceException;
import com.lifekhmer.common.exception.ResourceNotFoundException;
import com.lifekhmer.nutrition.dto.request.FoodRequest;
import com.lifekhmer.nutrition.dto.request.MealRequest;
import com.lifekhmer.nutrition.dto.response.FoodResponse;
import com.lifekhmer.nutrition.dto.response.MealResponse;
import com.lifekhmer.nutrition.dto.response.NutritionSummaryResponse;
import com.lifekhmer.nutrition.entity.Food;
import com.lifekhmer.nutrition.entity.Meal;
import com.lifekhmer.nutrition.repository.FoodRepository;
import com.lifekhmer.nutrition.repository.MealRepository;
import com.lifekhmer.nutrition.service.NutritionService;
import com.lifekhmer.security.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NutritionServiceImpl implements NutritionService {

    private final FoodRepository foods;
    private final MealRepository meals;
    private final UserRepository users;
    private final CurrentUserService current;

    @Override
    @Transactional
    public FoodResponse createFood(FoodRequest r) {
        String normalized = normalize(r.name());

        if (foods.existsByNormalizedName(normalized)) {
            throw new DuplicateResourceException("A food with this name already exists");
        }

        Food f = new Food();
        f.setName(r.name().trim());
        f.setNormalizedName(normalized);
        f.setCaloriesPer100g(r.caloriesPer100g());
        f.setProteinPer100g(r.proteinPer100g());
        f.setCarbohydratesPer100g(r.carbohydratesPer100g());
        f.setFatPer100g(r.fatPer100g());
        f.setSugarPer100g(r.sugarPer100g());
        f.setFiberPer100g(r.fiberPer100g());

        return food(foods.save(f));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FoodResponse> foods(String q, int p, int s) {
        Pageable page = PageRequest.of(p, Math.min(s, 100), Sort.by("name"));

        return foods
                .findByNormalizedNameContaining(normalize(q == null ? "" : q), page)
                .map(this::food);
    }

    @Override
    @Transactional(readOnly = true)
    public FoodResponse getFood(UUID id) {
        Food f = foods.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food not found"));
        return food(f);
    }

    @Override
    @Transactional
    public MealResponse createMeal(MealRequest r) {
        Meal m = new Meal();
        m.setUser(user());
        apply(m, r);

        return meal(meals.save(m));
    }

    @Override
    @Transactional
    public MealResponse updateMeal(UUID id, MealRequest r) {
        Meal m = owned(id);
        apply(m, r);

        return meal(m);
    }

    @Override
    @Transactional
    public void deleteMeal(UUID id) {
        meals.delete(owned(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MealResponse> meals(LocalDate from, LocalDate to, int p, int s) {
        LocalDateTime start = (from == null ? LocalDate.of(1970, 1, 1) : from).atStartOfDay();
        LocalDateTime end = (to == null ? LocalDate.now() : to).plusDays(1).atStartOfDay();

        Pageable pageable = PageRequest.of(
                p,
                Math.min(s, 100),
                Sort.by(Sort.Direction.DESC, "consumedAt")
        );

        return meals
                .findByUserIdAndConsumedAtBetween(current.getCurrentUserId(), start, end, pageable)
                .map(this::meal);
    }

    @Override
    @Transactional(readOnly = true)
    public NutritionSummaryResponse dailySummary(LocalDate date) {
        LocalDate d = date == null ? LocalDate.now() : date;

        List<Meal> list = meals
                .findByUserIdAndConsumedAtBetween(
                        current.getCurrentUserId(),
                        d.atStartOfDay(),
                        d.plusDays(1).atStartOfDay(),
                        Pageable.unpaged())
                .getContent();

        return new NutritionSummaryResponse(
                d,
                sum(list, Meal::getCalories),
                sum(list, Meal::getProtein),
                sum(list, Meal::getCarbohydrates),
                sum(list, Meal::getFat),
                sum(list, Meal::getSugar),
                sum(list, Meal::getFiber)
        );
    }

    private void apply(Meal m, MealRequest r) {
        Food f = foods.findById(r.foodId())
                .orElseThrow(() -> new ResourceNotFoundException("Food not found"));

        BigDecimal factor = r.portionGrams().divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);

        m.setFood(f);
        m.setFoodName(f.getName());
        m.setMealType(r.mealType());
        m.setPortionGrams(r.portionGrams());
        m.setCalories(f.getCaloriesPer100g().multiply(factor));
        m.setProtein(f.getProteinPer100g().multiply(factor));
        m.setCarbohydrates(f.getCarbohydratesPer100g().multiply(factor));
        m.setFat(f.getFatPer100g().multiply(factor));
        m.setSugar(f.getSugarPer100g().multiply(factor));
        m.setFiber(f.getFiberPer100g().multiply(factor));
        m.setConsumedAt(r.consumedAt());
        m.setNotes(r.notes());
    }

    private Meal owned(UUID id) {
        return meals.findByIdAndUserId(id, current.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found"));
    }

    private User user() {
        return users.findById(current.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User profile not found"));
    }

    private String normalize(String s) {
        return s.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " ");
    }

    private FoodResponse food(Food f) {
        return new FoodResponse(
                f.getId(),
                f.getName(),
                f.getCaloriesPer100g(),
                f.getProteinPer100g(),
                f.getCarbohydratesPer100g(),
                f.getFatPer100g(),
                f.getSugarPer100g(),
                f.getFiberPer100g()
        );
    }

    private MealResponse meal(Meal m) {
        return new MealResponse(
                m.getId(),
                m.getMealType(),
                m.getFoodName(),
                m.getPortionGrams(),
                m.getCalories(),
                m.getProtein(),
                m.getCarbohydrates(),
                m.getFat(),
                m.getSugar(),
                m.getFiber(),
                m.getConsumedAt(),
                m.getNotes()
        );
    }

    private BigDecimal sum(List<Meal> list, java.util.function.Function<Meal, BigDecimal> f) {
        return list.stream()
                .map(f)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
