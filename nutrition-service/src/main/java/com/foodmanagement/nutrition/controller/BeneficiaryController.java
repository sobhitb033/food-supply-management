
package com.foodmanagement.nutrition.controller;

import com.foodmanagement.nutrition.entity.Beneficiary;
import com.foodmanagement.nutrition.service.BeneficiaryService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/beneficiaries")
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;

    // constructor
    public BeneficiaryController(BeneficiaryService beneficiaryService) {
        this.beneficiaryService = beneficiaryService;
    }

    // register a beneficiary
    @PostMapping
    public ResponseEntity<Beneficiary> addBeneficiary(
            @RequestBody Beneficiary beneficiary) {
        Beneficiary saved = beneficiaryService.addBeneficiary(beneficiary);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // get all beneficiaries
    @GetMapping
    public List<Beneficiary> getAllBeneficiaries() {
        return beneficiaryService.getAllBeneficiaries();
    }

    // get one beneficiary by ID
    @GetMapping("/{id}")
    public Beneficiary getBeneficiaryById(@PathVariable Long id) {
        return beneficiaryService.getBeneficiaryById(id);
    }

    // search by part of a name
    @GetMapping("/search")
    public List<Beneficiary> searchBeneficiaries(
            @RequestParam String pattern) {
        return beneficiaryService.searchBeneficiaries(pattern);
    }

    // update a beneficiary
    @PutMapping("/{id}")
    public Beneficiary updateBeneficiary(
            @PathVariable Long id,
            @RequestBody Beneficiary beneficiary) {
        return beneficiaryService.updateBeneficiary(id, beneficiary);
    }

    // delete a beneficiary
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBeneficiary(@PathVariable Long id) {
        beneficiaryService.deleteBeneficiary(id);
        return ResponseEntity.noContent().build();
    }
}
