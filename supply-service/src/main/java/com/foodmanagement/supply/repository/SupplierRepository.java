package com.foodmanagement.supply.repository;

import com.foodmanagement.supply.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    // search for supplier names
    List<Supplier> findByNameContainingIgnoreCase(String pattern);
}
