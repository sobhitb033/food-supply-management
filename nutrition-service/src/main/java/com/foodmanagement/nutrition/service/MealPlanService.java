
package com.foodmanagement.nutrition.service;

import com.foodmanagement.nutrition.entity.*;
import com.foodmanagement.nutrition.repository.MealPlanRepository;
import com.foodmanagement.nutrition.repository.NutritionProgrammeRepository;
import com.foodmanagement.nutrition.repository.FoodItemRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MealPlanService {

    private final MealPlanRepository mealPlanRepository;
    private final NutritionProgrammeRepository programmeRepository;
    private final FoodItemRepository foodItemRepository;

    public MealPlanService(
            MealPlanRepository mealPlanRepository,
            NutritionProgrammeRepository programmeRepository,
            FoodItemRepository foodItemRepository) {
        this.mealPlanRepository = mealPlanRepository;
        this.programmeRepository = programmeRepository;
        this.foodItemRepository = foodItemRepository;
    }

    public MealPlan addMealPlan(
            Long programmeId, Long foodItemId, MealPlan mealPlan) {

        validate(mealPlan);

        // resolve ids to existing database entities
        NutritionProgramme programme = programmeRepository
                .findById(programmeId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Programme not found: " + programmeId));

        FoodItem foodItem = foodItemRepository
                .findById(foodItemId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Food item not found: " + foodItemId));

        mealPlan.setMealPlanId(null);
        mealPlan.setProgramme(programme);
        mealPlan.setFoodItem(foodItem);

        return mealPlanRepository.save(mealPlan);
    }

    @Transactional(readOnly = true)
    public List<MealPlan> getAllMealPlans() {
        return mealPlanRepository.findAll();
    }

    @Transactional(readOnly = true)
    public MealPlan getMealPlanById(Long id) {
        return mealPlanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Meal plan not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<MealPlan> getByProgramme(Long programmeId) {
        return mealPlanRepository.findByProgramme_ProgrammeId(programmeId);
    }

    public MealPlan updateMealPlan(Long id, MealPlan updated) {
        validate(updated);

        MealPlan existing = getMealPlanById(id);
        existing.setMealType(updated.getMealType());
        existing.setQuantity(updated.getQuantity());
        existing.setUnit(updated.getUnit());

        return mealPlanRepository.save(existing);
    }

    public void deleteMealPlan(Long id) {
        mealPlanRepository.delete(getMealPlanById(id));
    }

    private void validate(MealPlan mealPlan) {
        if (mealPlan == null
                || mealPlan.getMealType() == null
                || mealPlan.getMealType().isBlank()
                || mealPlan.getUnit() == null
                || mealPlan.getUnit().isBlank()
                || mealPlan.getQuantity() == null
                || mealPlan.getQuantity() <= 0) {
            throw new IllegalArgumentException(
                    "Meal type, positive quantity, and unit are required");
        }
    }
}
