
package com.foodmanagement.supply.service;

import com.foodmanagement.supply.entity.ProcurementRequest;
import com.foodmanagement.supply.entity.Supplier;
import com.foodmanagement.supply.repository.ProcurementRepository;
import com.foodmanagement.supply.repository.SupplierRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class ProcurementService {

    private final ProcurementRepository repository;
    private final SupplierRepository supplierRepository;

    private static final Set<String> VALID_STATUSES = Set.of("PENDING", "APPROVED", "REJECTED", "RECEIVED");

    public ProcurementService(
            ProcurementRepository repository,
            SupplierRepository supplierRepository) {
        this.repository = repository;
        this.supplierRepository = supplierRepository;
    }

    public ProcurementRequest createRequest(
            Long supplierId, ProcurementRequest request) {
        validate(request);

        // A request must reference a supplier that exists
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Supplier not found: " + supplierId));

        request.setRequestId(null);
        request.setSupplier(supplier);
        request.setRequestDate(java.time.LocalDate.now());
        request.setStatus("PENDING");

        return repository.save(request);
    }

    @Transactional(readOnly = true)
    public List<ProcurementRequest> getAllRequests() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public ProcurementRequest getRequestById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Procurement request not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<ProcurementRequest> getByStatus(String status) {
        if (status == null || !VALID_STATUSES.contains(status.toUpperCase())) {
            throw new IllegalArgumentException("Invalid procurement status");
        }
        return repository.findByStatusIgnoreCase(status.toUpperCase());
    }

    @Transactional(readOnly = true)
    public List<ProcurementRequest> searchByFoodName(String pattern) {
        if (pattern == null || pattern.isBlank()) {
            throw new IllegalArgumentException("Food name pattern is required");
        }
        return repository.findByFoodItemNameContainingIgnoreCase(pattern.trim());
    }

    @Transactional(readOnly = true)
    public List<ProcurementRequest> getBySupplier(Long supplierId) {
        // Reject an unknown supplier instead of silently returning an empty list
        if (!supplierRepository.existsById(supplierId)) {
            throw new IllegalArgumentException("Supplier not found: " + supplierId);
        }
        return repository.findBySupplier_SupplierId(supplierId);
    }

    public ProcurementRequest updateStatus(Long id, String status) {
        if (status == null || !VALID_STATUSES.contains(status.toUpperCase())) {
            throw new IllegalArgumentException(
                    "Status must be PENDING, APPROVED, REJECTED, or RECEIVED");
        }

        ProcurementRequest request = getRequestById(id);
        request.setStatus(status.toUpperCase());
        return repository.save(request);
    }

    public void deleteRequest(Long id) {
        repository.delete(getRequestById(id));
    }

    private void validate(ProcurementRequest request) {
        if (request == null
                || request.getFoodItemName() == null
                || request.getFoodItemName().isBlank()
                || request.getQuantity() == null
                || request.getQuantity() <= 0
                || request.getUnit() == null
                || request.getUnit().isBlank()
                || request.getEstimatedCost() == null
                || request.getEstimatedCost() < 0) {
            throw new IllegalArgumentException(
                    "Food name, positive quantity, unit, and non-negative cost are required");
        }
    }
}
