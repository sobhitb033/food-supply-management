
package com.foodmanagement.nutrition.controller;

import com.foodmanagement.nutrition.entity.NutritionProgramme;
import com.foodmanagement.nutrition.service.NutritionProgrammeService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/programmes")
public class NutritionProgrammeController {

    private final NutritionProgrammeService service;

    public NutritionProgrammeController(NutritionProgrammeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<NutritionProgramme> addProgramme(
            @RequestBody NutritionProgramme programme) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.addProgramme(programme));
    }

    @GetMapping
    public List<NutritionProgramme> getAllProgrammes() {
        return service.getAllProgrammes();
    }

    @GetMapping("/{id}")
    public NutritionProgramme getProgrammeById(@PathVariable Long id) {
        return service.getProgrammeById(id);
    }

    @GetMapping("/search")
    public List<NutritionProgramme> searchProgrammes(
            @RequestParam String pattern) {
        return service.searchProgrammes(pattern);
    }

    @PutMapping("/{id}")
    public NutritionProgramme updateProgramme(
            @PathVariable Long id,
            @RequestBody NutritionProgramme programme) {
        return service.updateProgramme(id, programme);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgramme(@PathVariable Long id) {
        service.deleteProgramme(id);
        return ResponseEntity.noContent().build();
    }
}
