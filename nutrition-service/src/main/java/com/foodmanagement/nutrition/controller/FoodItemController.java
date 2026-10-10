
package com.foodmanagement.nutrition.controller;

import com.foodmanagement.nutrition.entity.FoodItem;
import com.foodmanagement.nutrition.service.FoodItemService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food-items")
public class FoodItemController {

    private final FoodItemService foodItemService;

    public FoodItemController(FoodItemService foodItemService) {
        this.foodItemService = foodItemService;
    }

    @PostMapping
    public ResponseEntity<FoodItem> addFoodItem(@RequestBody FoodItem item) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(foodItemService.addFoodItem(item));
    }

    @GetMapping
    public List<FoodItem> getAllFoodItems() {
        return foodItemService.getAllFoodItems();
    }

    @GetMapping("/{id}")
    public FoodItem getFoodItemById(@PathVariable Long id) {
        return foodItemService.getFoodItemById(id);
    }

    @GetMapping("/search")
    public List<FoodItem> searchFoodItems(@RequestParam String pattern) {
        return foodItemService.searchFoodItems(pattern);
    }

    @PutMapping("/{id}")
    public FoodItem updateFoodItem(
            @PathVariable Long id,
            @RequestBody FoodItem item) {
        return foodItemService.updateFoodItem(id, item);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFoodItem(@PathVariable Long id) {
        foodItemService.deleteFoodItem(id);
        return ResponseEntity.noContent().build();
    }
}
