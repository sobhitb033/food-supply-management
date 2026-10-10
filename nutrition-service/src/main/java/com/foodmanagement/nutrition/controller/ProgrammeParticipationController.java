
package com.foodmanagement.nutrition.controller;

import com.foodmanagement.nutrition.entity.ProgrammeParticipation;
import com.foodmanagement.nutrition.service.ProgrammeParticipationService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/participation")
public class ProgrammeParticipationController {

    private final ProgrammeParticipationService service;

    public ProgrammeParticipationController(
            ProgrammeParticipationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProgrammeParticipation> enroll(
            @RequestParam Long beneficiaryId,
            @RequestParam Long programmeId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.enroll(beneficiaryId, programmeId));
    }

    @GetMapping
    public List<ProgrammeParticipation> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ProgrammeParticipation getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/programme/{programmeId}")
    public List<ProgrammeParticipation> getByProgramme(
            @PathVariable Long programmeId) {
        return service.getByProgramme(programmeId);
    }

    @GetMapping("/beneficiary/{beneficiaryId}")
    public List<ProgrammeParticipation> getByBeneficiary(
            @PathVariable Long beneficiaryId) {
        return service.getByBeneficiary(beneficiaryId);
    }

    @PatchMapping("/{id}/status")
    public ProgrammeParticipation updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return service.updateStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
