package com.foodmanagement.supply.service;

import com.foodmanagement.supply.entity.DistributionPlan;
import com.foodmanagement.supply.entity.InventoryBatch;
import com.foodmanagement.supply.repository.DistributionPlanRepository;
import com.foodmanagement.supply.repository.InventoryBatchRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class DistributionService {

    private final DistributionPlanRepository distributionRepository;
    private final InventoryBatchRepository inventoryRepository;

    // repositories needed for distribution and stock management
    public DistributionService(
            DistributionPlanRepository distributionRepository,
            InventoryBatchRepository inventoryRepository) {
        this.distributionRepository = distributionRepository;
        this.inventoryRepository = inventoryRepository;
    }

    // create a new distribution plan
    public DistributionPlan createPlan(DistributionPlan plan) {

        if (plan.getBeneficiaryId() == null || plan.getBeneficiaryId() <= 0) {
            throw new RuntimeException("Valid beneficiary ID is required");
        }

        if (plan.getInventoryBatch() == null
                || plan.getInventoryBatch().getBatchId() == null) {
            throw new RuntimeException("Inventory batch ID is required");
        }

        if (plan.getPlannedQuantity() == null
                || plan.getPlannedQuantity() <= 0) {
            throw new RuntimeException("Planned quantity must be greater than zero");
        }

        if (plan.getUnit() == null || plan.getUnit().isBlank()) {
            throw new RuntimeException("Unit is required");
        }

        if (plan.getPlannedDate() == null
                || plan.getPlannedDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Planned date cannot be in the past");
        }

        // fetch the real batch
        InventoryBatch batch = inventoryRepository
                .findById(plan.getInventoryBatch().getBatchId())
                .orElseThrow(() -> new RuntimeException("Inventory batch not found"));

        if (!batch.getUnit().equalsIgnoreCase(plan.getUnit())) {
            throw new RuntimeException("Distribution unit must match inventory unit");
        }

        if (batch.getExpiryDate().isBefore(plan.getPlannedDate())) {
            throw new RuntimeException("Batch expires before the planned distribution date");
        }

        plan.setInventoryBatch(batch);
        plan.setDeliveredQuantity(0.0);
        plan.setDeliveredDate(null);
        plan.setStatus("PLANNED");

        return distributionRepository.save(plan);
    }

    // retrieve all distribution plans
    public List<DistributionPlan> getAllPlans() {
        return distributionRepository.findAll();
    }

    // retrieve a plan using its ID
    public DistributionPlan getPlanById(Long id) {
        return distributionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Distribution plan not found"));
    }

    // retrieve plans belonging to a beneficiary
    public List<DistributionPlan> getPlansByBeneficiary(Long beneficiaryId) {
        return distributionRepository.findByBeneficiaryId(beneficiaryId);
    }

    // retrieve plans by status
    public List<DistributionPlan> getPlansByStatus(String status) {
        return distributionRepository.findByStatusIgnoreCase(status);
    }

    // retrieve plans for a particular date
    public List<DistributionPlan> getPlansByDate(LocalDate date) {
        return distributionRepository.findByPlannedDate(date);
    }

    // update status and deduct stock only when delivery is completed
    @Transactional
    public DistributionPlan updateStatus(
            Long id, String newStatus, Double deliveredQuantity) {

        DistributionPlan plan = getPlanById(id);

        String currentStatus = plan.getStatus();
        newStatus = newStatus.toUpperCase();

        if (currentStatus.equals("DELIVERED")
                || currentStatus.equals("CANCELLED")) {
            throw new RuntimeException(
                    "Delivered or cancelled plans cannot be changed");
        }

        if (!newStatus.equals("PLANNED")
                && !newStatus.equals("IN_TRANSIT")
                && !newStatus.equals("DELIVERED")
                && !newStatus.equals("CANCELLED")) {
            throw new RuntimeException("Invalid distribution status");
        }

        if (newStatus.equals("DELIVERED")) {

            if (deliveredQuantity == null || deliveredQuantity <= 0) {
                throw new RuntimeException(
                        "Delivered quantity must be greater than zero");
            }

            if (deliveredQuantity > plan.getPlannedQuantity()) {
                throw new RuntimeException(
                        "Delivered quantity cannot exceed planned quantity");
            }

            // fetch latest batch quantity before deducting stock
            InventoryBatch batch = inventoryRepository
                    .findById(plan.getInventoryBatch().getBatchId())
                    .orElseThrow(() -> new RuntimeException("Inventory batch not found"));

            if (batch.getExpiryDate().isBefore(LocalDate.now())) {
                throw new RuntimeException("Cannot distribute an expired batch");
            }

            if (batch.getQuantity() < deliveredQuantity) {
                throw new RuntimeException("Insufficient inventory stock");
            }

            // Deduct only the quantity actually delivered
            batch.setQuantity(batch.getQuantity() - deliveredQuantity);
            inventoryRepository.save(batch);

            plan.setInventoryBatch(batch);
            plan.setDeliveredQuantity(deliveredQuantity);
            plan.setDeliveredDate(LocalDate.now());
        }

        // Save the new status
        plan.setStatus(newStatus);

        return distributionRepository.save(plan);
    }

    // Delete a distribution plan that has not been completed
    public void deletePlan(Long id) {

        DistributionPlan plan = getPlanById(id);

        if (plan.getStatus().equals("DELIVERED")) {
            throw new RuntimeException(
                    "A delivered distribution plan cannot be deleted");
        }

        distributionRepository.delete(plan);
    }
}