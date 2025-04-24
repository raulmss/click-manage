package com.bezkoder.spring.inventory.controller;

import com.bezkoder.spring.inventory.dto.request.SupplierRequestDto;
import com.bezkoder.spring.inventory.dto.response.SupplierResponseDto;
import com.bezkoder.spring.inventory.service.SupplierService;
import com.bezkoder.spring.inventory.util.BusinessContextService;
import com.bezkoder.spring.security.jwt.models.Business;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;
    private final BusinessContextService businessContextService;

    @Operation(summary = "Create a new supplier")
    @PostMapping
    public ResponseEntity<SupplierResponseDto> createSupplier(@RequestBody SupplierRequestDto dto) {
        Business business = businessContextService.getCurrentBusiness();
        return ResponseEntity.ok(supplierService.create(business, dto));
    }

    @Operation(summary = "List or filter suppliers by name")
    @GetMapping
    public ResponseEntity<Page<SupplierResponseDto>> getSuppliers(
            @Parameter(description = "Name to filter by (optional)", example = "Supplier XYZ")
            @RequestParam(required = false) String name,
            @Parameter(description = "Page number (default is 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size (default is 10)", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        Business business = businessContextService.getCurrentBusiness();
        return ResponseEntity.ok(supplierService.findAll(business, name, page, size));
    }

    @Operation(summary = "Update a supplier by ID")
    @PutMapping("/{id}")
    public ResponseEntity<SupplierResponseDto> updateSupplier(
            @PathVariable Long id,
            @RequestBody SupplierRequestDto dto) {
        Business business = businessContextService.getCurrentBusiness();
        return ResponseEntity.ok(supplierService.update(id, business, dto));
    }

    @Operation(summary = "Delete a supplier by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        Business business = businessContextService.getCurrentBusiness();
        supplierService.delete(id, business);
        return ResponseEntity.noContent().build();
    }
}
