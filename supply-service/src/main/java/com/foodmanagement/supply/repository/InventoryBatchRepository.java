
package com.foodmanagement.supply.repository;

import com.foodmanagement.supply.entity.InventoryBatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface InventoryBatchRepository
        extends JpaRepository<InventoryBatch, Long> {

    List<InventoryBatch> findByQuantityLessThanEqual(Double threshold);

    List<InventoryBatch> findByExpiryDateBetween(
            LocalDate startDate, LocalDate endDate);

    List<InventoryBatch> findByFoodItemNameContainingIgnoreCase(String pattern);
}
