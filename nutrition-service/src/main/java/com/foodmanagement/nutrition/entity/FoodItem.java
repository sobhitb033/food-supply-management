
package com.foodmanagement.nutrition.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "food_items")
public class FoodItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long foodItemId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(nullable = false, length = 20)
    private String unit;

    // Nutritional values are measured per 100 grams
    @Column(nullable = false)
    private Double caloriesPer100g;

    @Column(nullable = false)
    private Double proteinPer100g;

    @Column(nullable = false)
    private Double carbsPer100g;

    @Column(nullable = false)
    private Double fatPer100g;

    public FoodItem() {
    }

    public Long getFoodItemId() {
        return foodItemId;
    }

    public void setFoodItemId(Long foodItemId) {
        this.foodItemId = foodItemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getCaloriesPer100g() {
        return caloriesPer100g;
    }

    public void setCaloriesPer100g(Double caloriesPer100g) {
        this.caloriesPer100g = caloriesPer100g;
    }

    public Double getProteinPer100g() {
        return proteinPer100g;
    }

    public void setProteinPer100g(Double proteinPer100g) {
        this.proteinPer100g = proteinPer100g;
    }

    public Double getCarbsPer100g() {
        return carbsPer100g;
    }

    public void setCarbsPer100g(Double carbsPer100g) {
        this.carbsPer100g = carbsPer100g;
    }

    public Double getFatPer100g() {
        return fatPer100g;
    }

    public void setFatPer100g(Double fatPer100g) {
        this.fatPer100g = fatPer100g;
    }
}
