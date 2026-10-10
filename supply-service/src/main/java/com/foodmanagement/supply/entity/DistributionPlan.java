
package com.foodmanagement.supply.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "distribution_plans")
public class DistributionPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long distributionId;

    // ID of the beneficiary managed by nutrition service
    @Column(nullable = false)
    private Long beneficiaryId;

    // linking the specific batch used for this distribution
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "batch_id", nullable = false)
    private InventoryBatch inventoryBatch;

    @Column(nullable = false)
    private Double plannedQuantity;

    // store the quantity actually handed over
    private Double deliveredQuantity = 0.0;

    @Column(nullable = false, length = 20)
    private String unit;

    @Column(nullable = false)
    private LocalDate plannedDate;

    private LocalDate deliveredDate;

    // PLANNED, IN_TRANSIT, DELIVERED, or CANCELLED
    @Column(nullable = false, length = 20)
    private String status = "PLANNED";

    public DistributionPlan() {
    }

    public Long getDistributionId() {
        return distributionId;
    }

    public void setDistributionId(Long distributionId) {
        this.distributionId = distributionId;
    }

    public Long getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(Long beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public InventoryBatch getInventoryBatch() {
        return inventoryBatch;
    }

    public void setInventoryBatch(InventoryBatch inventoryBatch) {
        this.inventoryBatch = inventoryBatch;
    }

    public Double getPlannedQuantity() {
        return plannedQuantity;
    }

    public void setPlannedQuantity(Double plannedQuantity) {
        this.plannedQuantity = plannedQuantity;
    }

    public Double getDeliveredQuantity() {
        return deliveredQuantity;
    }

    public void setDeliveredQuantity(Double deliveredQuantity) {
        this.deliveredQuantity = deliveredQuantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public LocalDate getPlannedDate() {
        return plannedDate;
    }

    public void setPlannedDate(LocalDate plannedDate) {
        this.plannedDate = plannedDate;
    }

    public LocalDate getDeliveredDate() {
        return deliveredDate;
    }

    public void setDeliveredDate(LocalDate deliveredDate) {
        this.deliveredDate = deliveredDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
