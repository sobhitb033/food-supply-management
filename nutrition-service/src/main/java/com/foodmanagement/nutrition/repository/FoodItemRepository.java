
package com.foodmanagement.nutrition.repository;

import com.foodmanagement.nutrition.entity.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {

    // search food items
    List<FoodItem> findByNameContainingIgnoreCase(String pattern);
}
