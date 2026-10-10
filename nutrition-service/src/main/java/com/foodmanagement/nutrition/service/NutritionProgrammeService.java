
package com.foodmanagement.nutrition.service;

import com.foodmanagement.nutrition.entity.NutritionProgramme;
import com.foodmanagement.nutrition.repository.NutritionProgrammeRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NutritionProgrammeService {

    private final NutritionProgrammeRepository repository;

    public NutritionProgrammeService(NutritionProgrammeRepository repository) {
        this.repository = repository;
    }

    public NutritionProgramme addProgramme(NutritionProgramme programme) {
        validate(programme);
        programme.setProgrammeId(null);
        return repository.save(programme);
    }

    @Transactional(readOnly = true)
    public List<NutritionProgramme> getAllProgrammes() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public NutritionProgramme getProgrammeById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Programme not found: " + id));
    }

    public NutritionProgramme updateProgramme(
            Long id, NutritionProgramme updated) {
        validate(updated);

        NutritionProgramme existing = getProgrammeById(id);
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setStartDate(updated.getStartDate());
        existing.setEndDate(updated.getEndDate());
        existing.setStatus(updated.getStatus());

        return repository.save(existing);
    }

    public void deleteProgramme(Long id) {
        repository.delete(getProgrammeById(id));
    }

    @Transactional(readOnly = true)
    public List<NutritionProgramme> searchProgrammes(String pattern) {
        if (pattern == null || pattern.isBlank()) {
            throw new IllegalArgumentException("Search pattern is required");
        }

        return repository.findByNameContainingIgnoreCase(pattern.trim());
    }

    private void validate(NutritionProgramme programme) {
        if (programme == null
                || programme.getName() == null
                || programme.getName().isBlank()
                || programme.getStartDate() == null
                || programme.getEndDate() == null) {
            throw new IllegalArgumentException(
                    "Programme name and dates are required");
        }

        if (programme.getEndDate().isBefore(programme.getStartDate())) {
            throw new IllegalArgumentException(
                    "End date cannot be before start date");
        }

        if (programme.getStatus() == null
                || !List.of("PLANNED", "ACTIVE", "COMPLETED")
                        .contains(programme.getStatus().toUpperCase())) {
            throw new IllegalArgumentException(
                    "Status must be PLANNED, ACTIVE, or COMPLETED");
        }

        // store status
        programme.setStatus(programme.getStatus().toUpperCase());
    }
}
