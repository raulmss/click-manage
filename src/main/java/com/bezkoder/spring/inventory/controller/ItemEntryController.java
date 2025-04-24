package com.bezkoder.spring.inventory.controller;

import com.bezkoder.spring.inventory.dto.request.ItemEntryRequestDto;
import com.bezkoder.spring.inventory.dto.response.ItemEntryResponseDto;
import com.bezkoder.spring.inventory.service.ItemEntryService;
import com.bezkoder.spring.inventory.util.BusinessContextService;
import com.bezkoder.spring.inventory.util.UserContextService;
import com.bezkoder.spring.security.jwt.models.Business;
import com.bezkoder.spring.security.jwt.models.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/item-entries")
@RequiredArgsConstructor
public class ItemEntryController {

    private final ItemEntryService itemEntryService;
    private final BusinessContextService businessContextService;
    private final UserContextService userContextService;

    @Operation(summary = "Create a new item entry")
    @PostMapping
    public ResponseEntity<ItemEntryResponseDto> create(@RequestBody ItemEntryRequestDto dto) {
        Business business = businessContextService.getCurrentBusiness();
        User user = userContextService.getCurrentUser();
        return ResponseEntity.ok(itemEntryService.create(dto, business, user));
    }

    @Operation(summary = "Filter or list item entries")
    @GetMapping
    public ResponseEntity<Page<ItemEntryResponseDto>> findAllFiltered(
            @Parameter(description = "Item name to filter by", example = "iPhone") @RequestParam(required = false) String itemName,
            @Parameter(description = "Supplier name to filter by", example = "TechSuppliers") @RequestParam(required = false) String supplierName,
            @Parameter(description = "User name to filter by", example = "john") @RequestParam(required = false) String userName,
            @Parameter(description = "Page number (default is 0)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size (default is 10)", example = "10") @RequestParam(defaultValue = "10") int size
    ) {
        Business business = businessContextService.getCurrentBusiness();
        return ResponseEntity.ok(itemEntryService.findAllFiltered(itemName, supplierName, userName, business, page, size));
    }

    @Operation(summary = "Find item entry by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ItemEntryResponseDto> findById(@PathVariable Long id) {
        Business business = businessContextService.getCurrentBusiness();
        return ResponseEntity.ok(itemEntryService.findById(id, business));
    }

    @Operation(summary = "Delete item entry by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Business business = businessContextService.getCurrentBusiness();
        itemEntryService.delete(id, business);
        return ResponseEntity.noContent().build();
    }
}
