
package com.foodmanagement.supply.repository;

import com.foodmanagement.supply.entity.ProcurementRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProcurementRepository
        extends JpaRepository<ProcurementRequest, Long> {

    // filter requests by their current processing status
    List<ProcurementRequest> findByStatusIgnoreCase(String status);

    // search requests by food item name
    List<ProcurementRequest> findByFoodItemNameContainingIgnoreCase(String pattern);

    // show all procurement requests from one supplier
    List<ProcurementRequest> findBySupplier_SupplierId(Long supplierId);
}
