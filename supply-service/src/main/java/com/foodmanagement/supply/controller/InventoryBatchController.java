
package com.foodmanagement.supply.controller;

import com.foodmanagement.supply.entity.InventoryBatch;
import com.foodmanagement.supply.service.InventoryBatchService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryBatchController {

    private final InventoryBatchService service;

    public InventoryBatchController(InventoryBatchService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<InventoryBatch> addBatch(
            @RequestParam Long supplierId,
            @RequestBody InventoryBatch batch) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.addBatch(supplierId, batch));
    }

    @GetMapping
    public List<InventoryBatch> getAllBatches() {
        return service.getAllBatches();
    }

    @GetMapping("/{id}")
    public InventoryBatch getBatchById(@PathVariable Long id) {
        return service.getBatchById(id);
    }

    @GetMapping("/low-stock")
    public List<InventoryBatch> getLowStockBatches() {
        return service.getLowStockBatches();
    }

    @GetMapping("/expiring")
    public List<InventoryBatch> getExpiringBatches(
            @RequestParam(defaultValue = "30") int days) {
        return service.getExpiringBatches(days);
    }

    @GetMapping("/search")
    public List<InventoryBatch> searchByFoodName(@RequestParam String pattern) {
        return service.searchByFoodName(pattern);
    }

    @PutMapping("/{id}")
    public InventoryBatch updateBatch(
            @PathVariable Long id,
            @RequestBody InventoryBatch batch) {
        return service.updateBatch(id, batch);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBatch(@PathVariable Long id) {
        service.deleteBatch(id);
        return ResponseEntity.noContent().build();
    }
}
