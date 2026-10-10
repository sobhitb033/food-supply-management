
package com.foodmanagement.nutrition.repository;

import com.foodmanagement.nutrition.entity.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeneficiaryRepository
        extends JpaRepository<Beneficiary, Long> {

    // search names cases
    List<Beneficiary> findByFullNameContainingIgnoreCase(String pattern);
}
