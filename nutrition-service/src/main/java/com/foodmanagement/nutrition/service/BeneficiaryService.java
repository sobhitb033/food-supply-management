
package com.foodmanagement.nutrition.service;

import com.foodmanagement.nutrition.entity.Beneficiary;
import com.foodmanagement.nutrition.repository.BeneficiaryRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BeneficiaryService {

    private final BeneficiaryRepository beneficiaryRepository;

    // constructor 
    public BeneficiaryService(BeneficiaryRepository beneficiaryRepository) {
        this.beneficiaryRepository = beneficiaryRepository;
    }

    // register a new beneficiary
    public Beneficiary addBeneficiary(Beneficiary beneficiary) {
        validateBeneficiary(beneficiary);
        beneficiary.setBeneficiaryId(null); // Ensure a new record is created
        return beneficiaryRepository.save(beneficiary);
    }

    // retrieve all beneficiaries
    @Transactional(readOnly = true)
    public List<Beneficiary> getAllBeneficiaries() {
        return beneficiaryRepository.findAll();
    }

    // retrieve a beneficiary by ID
    @Transactional(readOnly = true)
    public Beneficiary getBeneficiaryById(Long id) {
        return beneficiaryRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Beneficiary not found with ID: " + id));
    }

    // update an existing beneficiary
    public Beneficiary updateBeneficiary(Long id, Beneficiary updated) {
        validateBeneficiary(updated);

        Beneficiary existing = getBeneficiaryById(id);

        existing.setFullName(updated.getFullName());
        existing.setAge(updated.getAge());
        existing.setGender(updated.getGender());
        existing.setAddress(updated.getAddress());
        existing.setEligibilityStatus(updated.getEligibilityStatus());

        return beneficiaryRepository.save(existing);
    }

    // delete a beneficiary if it exists
    public void deleteBeneficiary(Long id) {
        Beneficiary existing = getBeneficiaryById(id);
        beneficiaryRepository.delete(existing);
    }

    // search by a name pattern
    @Transactional(readOnly = true)
    public List<Beneficiary> searchBeneficiaries(String pattern) {
        if (pattern == null || pattern.isBlank()) {
            throw new IllegalArgumentException(
                    "Search pattern must not be empty");
        }

        return beneficiaryRepository
                .findByFullNameContainingIgnoreCase(pattern.trim());
    }

    // Validate input before saving it
    private void validateBeneficiary(Beneficiary beneficiary) {
        if (beneficiary == null) {
            throw new IllegalArgumentException(
                    "Beneficiary details are required");
        }

        if (beneficiary.getFullName() == null
                || beneficiary.getFullName().isBlank()) {
            throw new IllegalArgumentException(
                    "Beneficiary name is required");
        }

        if (beneficiary.getAge() == null || beneficiary.getAge() < 0) {
            throw new IllegalArgumentException(
                    "Age must be zero or greater");
        }

        if (beneficiary.getEligibilityStatus() == null
                || beneficiary.getEligibilityStatus().isBlank()) {
            throw new IllegalArgumentException(
                    "Eligibility status is required");
        }
    }
}
