
package com.foodmanagement.nutrition.service;

import com.foodmanagement.nutrition.entity.*;
import com.foodmanagement.nutrition.repository.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProgrammeParticipationService {

    private final ProgrammeParticipationRepository participationRepository;
    private final BeneficiaryRepository beneficiaryRepository;
    private final NutritionProgrammeRepository programmeRepository;

    public ProgrammeParticipationService(
            ProgrammeParticipationRepository participationRepository,
            BeneficiaryRepository beneficiaryRepository,
            NutritionProgrammeRepository programmeRepository) {
        this.participationRepository = participationRepository;
        this.beneficiaryRepository = beneficiaryRepository;
        this.programmeRepository = programmeRepository;
    }

    public ProgrammeParticipation enroll(
            Long beneficiaryId, Long programmeId) {

        Beneficiary beneficiary = beneficiaryRepository.findById(beneficiaryId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Beneficiary not found: " + beneficiaryId));

        NutritionProgramme programme = programmeRepository.findById(programmeId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Programme not found: " + programmeId));

        if (participationRepository
                .existsByBeneficiary_BeneficiaryIdAndProgramme_ProgrammeId(
                        beneficiaryId, programmeId)) {
            throw new IllegalArgumentException(
                    "Beneficiary is already enrolled in this programme");
        }

        ProgrammeParticipation participation = new ProgrammeParticipation();
        participation.setBeneficiary(beneficiary);
        participation.setProgramme(programme);

        return participationRepository.save(participation);
    }

    @Transactional(readOnly = true)
    public List<ProgrammeParticipation> getAll() {
        return participationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ProgrammeParticipation getById(Long id) {
        return participationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Participation not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<ProgrammeParticipation> getByProgramme(Long programmeId) {
        return participationRepository
                .findByProgramme_ProgrammeId(programmeId);
    }

    @Transactional(readOnly = true)
    public List<ProgrammeParticipation> getByBeneficiary(Long beneficiaryId) {
        return participationRepository
                .findByBeneficiary_BeneficiaryId(beneficiaryId);
    }

    public ProgrammeParticipation updateStatus(Long id, String status) {
        if (status == null || !List.of("ACTIVE", "COMPLETED", "WITHDRAWN")
                .contains(status.toUpperCase())) {
            throw new IllegalArgumentException(
                    "Status must be ACTIVE, COMPLETED, or WITHDRAWN");
        }

        ProgrammeParticipation participation = getById(id);
        participation.setStatus(status.toUpperCase());

        return participationRepository.save(participation);
    }

    public void delete(Long id) {
        participationRepository.delete(getById(id));
    }
}
