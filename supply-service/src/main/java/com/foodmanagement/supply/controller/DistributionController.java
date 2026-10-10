package com.foodmanagement.supply.controller;

import com.foodmanagement.supply.entity.DistributionPlan;
import com.foodmanagement.supply.service.DistributionService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/distributions")
public class DistributionController {

    private final DistributionService distributionService;

    // services that handles distribution operations
    public DistributionController(DistributionService distributionService) {
        this.distributionService = distributionService;
    }

    // create a distribution plan
    @PostMapping
    public DistributionPlan createPlan(@RequestBody DistributionPlan plan) {
        return distributionService.createPlan(plan);
    }

    // get all distribution plans
    @GetMapping
    public List<DistributionPlan> getAllPlans() {
        return distributionService.getAllPlans();
    }

    // get a distribution plan by ID
    @GetMapping("/{id}")
    public DistributionPlan getPlanById(@PathVariable Long id) {
        return distributionService.getPlanById(id);
    }

    // get all plans for a beneficiary
    @GetMapping("/beneficiary/{beneficiaryId}")
    public List<DistributionPlan> getPlansByBeneficiary(
            @PathVariable Long beneficiaryId) {
        return distributionService.getPlansByBeneficiary(beneficiaryId);
    }

    // filter plans by status
    @GetMapping("/status/{status}")
    public List<DistributionPlan> getPlansByStatus(
            @PathVariable String status) {
        return distributionService.getPlansByStatus(status);
    }

    // filter plans by planned date
    @GetMapping("/date/{date}")
    public List<DistributionPlan> getPlansByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return distributionService.getPlansByDate(date);
    }

    // update status; provide deliveredQuantity when marking DELIVERED
    @PatchMapping("/{id}/status")
    public DistributionPlan updateStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) Double deliveredQuantity) {
        return distributionService.updateStatus(
                id, status, deliveredQuantity);
    }

    // delete a plan that has not been delivered
    @DeleteMapping("/{id}")
    public void deletePlan(@PathVariable Long id) {
        distributionService.deletePlan(id);
    }
}