
package com.foodmanagement.supply.service;

import com.foodmanagement.supply.entity.Supplier;
import com.foodmanagement.supply.repository.SupplierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SupplierService {

    private final SupplierRepository repository;

    public SupplierService(SupplierRepository repository) {
        this.repository = repository;
    }

    public Supplier addSupplier(Supplier supplier) {
        validate(supplier);
        supplier.setSupplierId(null);
        supplier.setStatus("ACTIVE");
        return repository.save(supplier);
    }

    @Transactional(readOnly = true)
    public List<Supplier> getAllSuppliers() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Supplier getSupplierById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found: " + id));
    }

    public Supplier updateSupplier(Long id, Supplier updated) {
        validate(updated);

        Supplier existing = getSupplierById(id);
        existing.setName(updated.getName());
        existing.setContactPerson(updated.getContactPerson());
        existing.setPhone(updated.getPhone());
        existing.setEmail(updated.getEmail());
        existing.setAddress(updated.getAddress());
        existing.setStatus(updated.getStatus());

        return repository.save(existing);
    }

    public void deleteSupplier(Long id) {
        repository.delete(getSupplierById(id));
    }

    @Transactional(readOnly = true)
    public List<Supplier> searchSuppliers(String pattern) {
        if (pattern == null || pattern.isBlank()) {
            throw new IllegalArgumentException("Search pattern is required");
        }

        return repository.findByNameContainingIgnoreCase(pattern.trim());
    }

    private void validate(Supplier supplier) {
        if (supplier == null
                || supplier.getName() == null || supplier.getName().isBlank()
                || supplier.getPhone() == null || supplier.getPhone().isBlank()
                || supplier.getAddress() == null || supplier.getAddress().isBlank()) {
            throw new IllegalArgumentException(
                    "Supplier name, phone, and address are required");
        }

        if (supplier.getStatus() == null
                || !List.of("ACTIVE", "INACTIVE")
                        .contains(supplier.getStatus().toUpperCase())) {
            throw new IllegalArgumentException(
                    "Status must be ACTIVE or INACTIVE");
        }

        supplier.setStatus(supplier.getStatus().toUpperCase());
    }
}
