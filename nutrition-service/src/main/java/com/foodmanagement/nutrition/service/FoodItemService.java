
package com.foodmanagement.nutrition.service;

import com.foodmanagement.nutrition.entity.FoodItem;
import com.foodmanagement.nutrition.repository.FoodItemRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FoodItemService {

    private final FoodItemRepository foodItemRepository;

    public FoodItemService(FoodItemRepository foodItemRepository) {
        this.foodItemRepository = foodItemRepository;
    }

    public FoodItem addFoodItem(FoodItem item) {
        validate(item);
        item.setFoodItemId(null);
        return foodItemRepository.save(item);
    }

    @Transactional(readOnly = true)
    public List<FoodItem> getAllFoodItems() {
        return foodItemRepository.findAll();
    }

    @Transactional(readOnly = true)
    public FoodItem getFoodItemById(Long id) {
        return foodItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Food item not found: " + id));
    }

    public FoodItem updateFoodItem(Long id, FoodItem updated) {
        validate(updated);

        FoodItem existing = getFoodItemById(id);
        existing.setName(updated.getName());
        existing.setCategory(updated.getCategory());
        existing.setUnit(updated.getUnit());
        existing.setCaloriesPer100g(updated.getCaloriesPer100g());
        existing.setProteinPer100g(updated.getProteinPer100g());
        existing.setCarbsPer100g(updated.getCarbsPer100g());
        existing.setFatPer100g(updated.getFatPer100g());

        return foodItemRepository.save(existing);
    }

    public void deleteFoodItem(Long id) {
        foodItemRepository.delete(getFoodItemById(id));
    }

    @Transactional(readOnly = true)
    public List<FoodItem> searchFoodItems(String pattern) {
        if (pattern == null || pattern.isBlank()) {
            throw new IllegalArgumentException("Search pattern is required");
        }

        return foodItemRepository.findByNameContainingIgnoreCase(pattern.trim());
    }

    private void validate(FoodItem item) {
        if (item == null
                || item.getName() == null || item.getName().isBlank()
                || item.getCategory() == null || item.getCategory().isBlank()
                || item.getUnit() == null || item.getUnit().isBlank()) {
            throw new IllegalArgumentException(
                    "Food name, category and unit are required");
        }

        if (item.getCaloriesPer100g() == null || item.getCaloriesPer100g() < 0
                || item.getProteinPer100g() == null || item.getProteinPer100g() < 0
                || item.getCarbsPer100g() == null || item.getCarbsPer100g() < 0
                || item.getFatPer100g() == null || item.getFatPer100g() < 0) {
            throw new IllegalArgumentException(
                    "Nutritional values must be zero or greater");
        }
    }
}
