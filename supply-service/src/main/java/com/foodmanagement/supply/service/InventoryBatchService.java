
package com.foodmanagement.supply.service;

import com.foodmanagement.supply.entity.InventoryBatch;
import com.foodmanagement.supply.entity.Supplier;
import com.foodmanagement.supply.repository.InventoryBatchRepository;
import com.foodmanagement.supply.repository.SupplierRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class InventoryBatchService {

    private final InventoryBatchRepository batchRepository;
    private final SupplierRepository supplierRepository;

    public InventoryBatchService(
            InventoryBatchRepository batchRepository,
            SupplierRepository supplierRepository) {
        this.batchRepository = batchRepository;
        this.supplierRepository = supplierRepository;
    }

    public InventoryBatch addBatch(Long supplierId, InventoryBatch batch) {
        validate(batch);

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found: " + supplierId));

        if (batch.getReceivedDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Received date cannot be in the future");
        }

        if (batch.getExpiryDate().isBefore(batch.getReceivedDate())) {
            throw new IllegalArgumentException("Expiry date cannot precede received date");
        }

        batch.setBatchId(null);
        batch.setSupplier(supplier);
        return batchRepository.save(batch);
    }

    @Transactional(readOnly = true)
    public List<InventoryBatch> getAllBatches() {
        return batchRepository.findAll();
    }

    @Transactional(readOnly = true)
    public InventoryBatch getBatchById(Long id) {
        return batchRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Batch not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<InventoryBatch> getLowStockBatches() {
        return batchRepository.findAll().stream()
                .filter(batch -> batch.getQuantity() <= batch.getMinimumStock())
                .toList();
    }

    @Transactional(readOnly = true)
    public List<InventoryBatch> getExpiringBatches(int days) {
        if (days < 0) {
            throw new IllegalArgumentException("Days cannot be negative");
        }

        LocalDate today = LocalDate.now();
        return batchRepository.findByExpiryDateBetween(today, today.plusDays(days));
    }

    @Transactional(readOnly = true)
    public List<InventoryBatch> searchByFoodName(String pattern) {
        if (pattern == null || pattern.isBlank()) {
            throw new IllegalArgumentException("Food name pattern is required");
        }

        return batchRepository
                .findByFoodItemNameContainingIgnoreCase(pattern.trim());
    }

    public InventoryBatch updateBatch(Long id, InventoryBatch updated) {
        validate(updated);

        InventoryBatch existing = getBatchById(id);
        existing.setFoodItemName(updated.getFoodItemName());
        existing.setBatchNumber(updated.getBatchNumber());
        existing.setQuantity(updated.getQuantity());
        existing.setMinimumStock(updated.getMinimumStock());
        existing.setUnit(updated.getUnit());
        existing.setReceivedDate(updated.getReceivedDate());
        existing.setExpiryDate(updated.getExpiryDate());

        return batchRepository.save(existing);
    }

    public void deleteBatch(Long id) {
        batchRepository.delete(getBatchById(id));
    }

    private void validate(InventoryBatch batch) {
        if (batch == null
                || batch.getFoodItemName() == null || batch.getFoodItemName().isBlank()
                || batch.getBatchNumber() == null || batch.getBatchNumber().isBlank()
                || batch.getQuantity() == null || batch.getQuantity() < 0
                || batch.getMinimumStock() == null || batch.getMinimumStock() < 0
                || batch.getUnit() == null || batch.getUnit().isBlank()
                || batch.getReceivedDate() == null
                || batch.getExpiryDate() == null) {
            throw new IllegalArgumentException("All batch fields are required and quantities cannot be negative");
        }
    }
}
