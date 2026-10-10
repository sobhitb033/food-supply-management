
package com.foodmanagement.nutrition.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "programme_participation", uniqueConstraints = @UniqueConstraint(columnNames = { "beneficiary_id",
        "programme_id" }))
public class ProgrammeParticipation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long participationId;

    // one beneficiary can join many programmes
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "beneficiary_id", nullable = false)
    private Beneficiary beneficiary;

    // one programme can have many participants
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "programme_id", nullable = false)
    private NutritionProgramme programme;

    @Column(nullable = false)
    private LocalDate enrollmentDate = LocalDate.now();

    @Column(nullable = false, length = 20)
    private String status = "ACTIVE";

    public ProgrammeParticipation() {
    }

    public Long getParticipationId() {
        return participationId;
    }

    public void setParticipationId(Long participationId) {
        this.participationId = participationId;
    }

    public Beneficiary getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(Beneficiary beneficiary) {
        this.beneficiary = beneficiary;
    }

    public NutritionProgramme getProgramme() {
        return programme;
    }

    public void setProgramme(NutritionProgramme programme) {
        this.programme = programme;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
