
package com.foodmanagement.nutrition.repository;

import com.foodmanagement.nutrition.entity.NutritionProgramme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NutritionProgrammeRepository
        extends JpaRepository<NutritionProgramme, Long> {

    // search programme names
    List<NutritionProgramme> findByNameContainingIgnoreCase(String pattern);
}
