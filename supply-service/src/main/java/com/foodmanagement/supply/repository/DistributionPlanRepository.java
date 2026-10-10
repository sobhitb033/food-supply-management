
package com.foodmanagement.supply.repository;

import com.foodmanagement.supply.entity.DistributionPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DistributionPlanRepository
        extends JpaRepository<DistributionPlan, Long> {

    // find all distributions assigned to a beneficiary
    List<DistributionPlan> findByBeneficiaryId(Long beneficiaryId);

    // filter distributions by their current status
    List<DistributionPlan> findByStatusIgnoreCase(String status);

    // find distributions planned for a particular date
    List<DistributionPlan> findByPlannedDate(java.time.LocalDate plannedDate);

    // find all distributions using an inventory batch
    List<DistributionPlan> findByInventoryBatch_BatchId(Long batchId);
}
