package com.bezkoder.spring.inventory.controller;

import com.bezkoder.spring.inventory.dto.request.ItemRequestDto;
import com.bezkoder.spring.inventory.dto.response.ItemResponseDto;
import com.bezkoder.spring.inventory.mapper.ItemMapper;
import com.bezkoder.spring.inventory.model.Item;
import com.bezkoder.spring.inventory.service.ItemService;
import com.bezkoder.spring.inventory.util.BusinessContextService;
import com.bezkoder.spring.security.jwt.models.Business;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
@Tag(name = "Item", description = "Operations related to stock items")
public class ItemController {

    private final ItemService itemService;
    private final BusinessContextService businessContextService;
    private final ItemMapper itemMapper;

    @PostMapping
    @Operation(summary = "Create a new item")
    public ResponseEntity<ItemResponseDto> create(@RequestBody ItemRequestDto dto) {
        Business business = businessContextService.getCurrentBusiness();
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.create(dto, business));
    }

    @Operation(summary = "Filter or list Items", description = "Retrieves a paginated list of Items filtered by name or barcode for the current business")
    @GetMapping
    public ResponseEntity<Page<ItemResponseDto>> filterItems(
            @Parameter(description = "Name to filter by (optional)", example = "Laptop") @RequestParam(required = false) String name,
            @Parameter(description = "Bar code to filter by (optional)", example = "123456789") @RequestParam(required = false) String barCode,
            @Parameter(description = "Item type name to filter by (optional)", example = "medicine") @RequestParam(required = false) String itemTypeName,
            @Parameter(description = "Page number (default is 0)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size (default is 10)", example = "10") @RequestParam(defaultValue = "10") int size
    ) {
        Business business = businessContextService.getCurrentBusiness();
        return ResponseEntity.ok(itemService.filterItems(business, name, barCode, itemTypeName, page, size));
    }



    @GetMapping("/{id}")
    @Operation(summary = "Get an item by its ID")
    public ResponseEntity<ItemResponseDto> findById(@PathVariable Long id) {
        Business business = businessContextService.getCurrentBusiness();
        return ResponseEntity.ok(itemService.findByIdAndBusiness(id, business));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing item by ID")
    public ResponseEntity<ItemResponseDto> update(@PathVariable Long id, @RequestBody ItemRequestDto dto) {
        Business business = businessContextService.getCurrentBusiness();
        return ResponseEntity.ok(itemService.update(id, dto, business));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an item by ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Business business = businessContextService.getCurrentBusiness();
        itemService.delete(id, business);
        return ResponseEntity.noContent().build();
    }
}
