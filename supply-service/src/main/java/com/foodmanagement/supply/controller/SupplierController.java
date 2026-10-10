
package com.foodmanagement.supply.controller;

import com.foodmanagement.supply.entity.Supplier;
import com.foodmanagement.supply.service.SupplierService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    private final SupplierService service;

    public SupplierController(SupplierService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Supplier> addSupplier(@RequestBody Supplier supplier) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.addSupplier(supplier));
    }

    @GetMapping
    public List<Supplier> getAllSuppliers() {
        return service.getAllSuppliers();
    }

    @GetMapping("/{id}")
    public Supplier getSupplierById(@PathVariable Long id) {
        return service.getSupplierById(id);
    }

    @GetMapping("/search")
    public List<Supplier> searchSuppliers(@RequestParam String pattern) {
        return service.searchSuppliers(pattern);
    }

    @PutMapping("/{id}")
    public Supplier updateSupplier(
            @PathVariable Long id,
            @RequestBody Supplier supplier) {
        return service.updateSupplier(id, supplier);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        service.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }
}
