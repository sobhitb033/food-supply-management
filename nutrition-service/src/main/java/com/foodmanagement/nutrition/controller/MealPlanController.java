
package com.foodmanagement.nutrition.controller;

import com.foodmanagement.nutrition.entity.MealPlan;
import com.foodmanagement.nutrition.service.MealPlanService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meal-plans")
public class MealPlanController {

    private final MealPlanService service;

    public MealPlanController(MealPlanService service) {
        this.service = service;
    }

    // create a meal plan with existing programme and food ids
    @PostMapping
    public ResponseEntity<MealPlan> addMealPlan(
            @RequestParam Long programmeId,
            @RequestParam Long foodItemId,
            @RequestBody MealPlan mealPlan) {

        MealPlan saved = service.addMealPlan(
                programmeId, foodItemId, mealPlan);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public List<MealPlan> getAllMealPlans() {
        return service.getAllMealPlans();
    }

    @GetMapping("/{id}")
    public MealPlan getMealPlanById(@PathVariable Long id) {
        return service.getMealPlanById(id);
    }

    @GetMapping("/programme/{programmeId}")
    public List<MealPlan> getByProgramme(
            @PathVariable Long programmeId) {
        return service.getByProgramme(programmeId);
    }

    @PutMapping("/{id}")
    public MealPlan updateMealPlan(
            @PathVariable Long id,
            @RequestBody MealPlan mealPlan) {
        return service.updateMealPlan(id, mealPlan);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMealPlan(@PathVariable Long id) {
        service.deleteMealPlan(id);
        return ResponseEntity.noContent().build();
    }
}
