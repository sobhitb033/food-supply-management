
package com.foodmanagement.nutrition.repository;

import com.foodmanagement.nutrition.entity.ProgrammeParticipation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgrammeParticipationRepository
        extends JpaRepository<ProgrammeParticipation, Long> {

    List<ProgrammeParticipation> findByProgramme_ProgrammeId(Long programmeId);

    List<ProgrammeParticipation> findByBeneficiary_BeneficiaryId(Long beneficiaryId);

    boolean existsByBeneficiary_BeneficiaryIdAndProgramme_ProgrammeId(
            Long beneficiaryId, Long programmeId);
}
