
package com.foodmanagement.supply.controller;

import com.foodmanagement.supply.entity.ProcurementRequest;
import com.foodmanagement.supply.service.ProcurementService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/procurement")
public class ProcurementController {

    private final ProcurementService service;

    public ProcurementController(ProcurementService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProcurementRequest> createRequest(
            @RequestParam Long supplierId,
            @RequestBody ProcurementRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createRequest(supplierId, request));
    }

    @GetMapping
    public List<ProcurementRequest> getAllRequests() {
        return service.getAllRequests();
    }

    @GetMapping("/{id}")
    public ProcurementRequest getRequestById(@PathVariable Long id) {
        return service.getRequestById(id);
    }

    @GetMapping("/status/{status}")
    public List<ProcurementRequest> getByStatus(@PathVariable String status) {
        return service.getByStatus(status);
    }

    @GetMapping("/search")
    public List<ProcurementRequest> searchByFoodName(
            @RequestParam String pattern) {
        return service.searchByFoodName(pattern);
    }

    @GetMapping("/supplier/{supplierId}")
    public List<ProcurementRequest> getBySupplier(
            @PathVariable Long supplierId) {
        return service.getBySupplier(supplierId);
    }

    @PatchMapping("/{id}/status")
    public ProcurementRequest updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return service.updateStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable Long id) {
        service.deleteRequest(id);
        return ResponseEntity.noContent().build();
    }
}
