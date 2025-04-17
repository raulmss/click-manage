package com.bezkoder.spring.inventory.controller;

import com.bezkoder.spring.inventory.dto.request.ItemTypeRequestDto;
import com.bezkoder.spring.inventory.dto.response.ItemTypeResponseDto;
import com.bezkoder.spring.inventory.service.ItemTypeService;
import com.bezkoder.spring.security.jwt.models.Business;
import com.bezkoder.spring.inventory.util.BusinessContextService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/item-types")
@RequiredArgsConstructor
public class ItemTypeController {

    private final ItemTypeService itemTypeService;
    private final BusinessContextService businessContextService;

    @Operation(summary = "Create a new Item Type", description = "Creates a new Item Type for the current business")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Item Type created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or item type already exists")
    })
    @PostMapping
    public ResponseEntity<ItemTypeResponseDto> create(
            @RequestBody ItemTypeRequestDto dto
    ) {
        Business business = businessContextService.getCurrentBusiness();
        return ResponseEntity.status(HttpStatus.CREATED).body(itemTypeService.create(dto, business));
    }

    @Operation(summary = "Filter or list Item Types", description = "Retrieves a paginated list of Item Types filtered by name for the current business")
    @GetMapping
    public ResponseEntity<Page<ItemTypeResponseDto>> filterItemTypes(
            @Parameter(description = "Name to filter by (optional)", example = "Electronics") @RequestParam(required = false) String name,
            @Parameter(description = "Page number (default is 0)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size (default is 10)", example = "10") @RequestParam(defaultValue = "10") int size
    ) {
        Business business = businessContextService.getCurrentBusiness();
        return ResponseEntity.ok(itemTypeService.filterItemTypeByName(business, name, page, size));
    }

    @Operation(summary = "Get Item Type by ID", description = "Retrieves a specific Item Type by its ID for the current business")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item Type found"),
            @ApiResponse(responseCode = "404", description = "Item Type not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ItemTypeResponseDto> findById(
            @Parameter(description = "ID of the Item Type") @PathVariable Long id
    ) {
        Business business = businessContextService.getCurrentBusiness();
        return ResponseEntity.ok(itemTypeService.findByIdAndBusiness(id, business));
    }

    @Operation(summary = "Update Item Type", description = "Updates the details of an existing Item Type for the current business")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item Type updated successfully"),
            @ApiResponse(responseCode = "404", description = "Item Type not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ItemTypeResponseDto> update(
            @Parameter(description = "ID of the Item Type to update") @PathVariable Long id,
            @RequestBody ItemTypeRequestDto dto
    ) {
        Business business = businessContextService.getCurrentBusiness();
        return ResponseEntity.ok(itemTypeService.update(id, dto, business));
    }

    @Operation(summary = "Delete Item Type", description = "Deletes an existing Item Type for the current business")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Item Type deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Item Type not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID of the Item Type to delete") @PathVariable Long id
    ) {
        Business business = businessContextService.getCurrentBusiness();
        itemTypeService.delete(id, business);
        return ResponseEntity.noContent().build();
    }
}
