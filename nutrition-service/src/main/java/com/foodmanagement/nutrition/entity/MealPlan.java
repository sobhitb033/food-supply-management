
package com.foodmanagement.nutrition.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "meal_plans")
public class MealPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mealPlanId;

    // many to one relationship between meal plans and programme
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "programme_id", nullable = false)
    private NutritionProgramme programme;

    // many to one relationship between meal plans and food items
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "food_item_id", nullable = false)
    private FoodItem foodItem;

    @Column(nullable = false, length = 30)
    private String mealType;

    @Column(nullable = false)
    private Double quantity;

    @Column(nullable = false, length = 20)
    private String unit;

    public MealPlan() {
    }

    public Long getMealPlanId() {
        return mealPlanId;
    }

    public void setMealPlanId(Long mealPlanId) {
        this.mealPlanId = mealPlanId;
    }

    public NutritionProgramme getProgramme() {
        return programme;
    }

    public void setProgramme(NutritionProgramme programme) {
        this.programme = programme;
    }

    public FoodItem getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(FoodItem foodItem) {
        this.foodItem = foodItem;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
