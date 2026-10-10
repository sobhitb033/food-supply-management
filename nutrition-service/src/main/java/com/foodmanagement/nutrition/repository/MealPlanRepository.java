
package com.foodmanagement.nutrition.repository;

import com.foodmanagement.nutrition.entity.MealPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MealPlanRepository extends JpaRepository<MealPlan, Long> {

    // find all meal plans which belong to a programme
    List<MealPlan> findByProgramme_ProgrammeId(Long programmeId);

    // find meal plans for a particular food item
    List<MealPlan> findByFoodItem_FoodItemId(Long foodItemId);
}
