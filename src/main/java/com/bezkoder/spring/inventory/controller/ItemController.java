package com.bezkoder.spring.inventory.controller;

import com.bezkoder.spring.inventory.dto.request.ItemRequestDto;
import com.bezkoder.spring.inventory.dto.response.ItemResponseDto;
import com.bezkoder.spring.inventory.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Tag(name = "Items", description = "Operations related to items")
public class ItemController {

    private final ItemService itemService;

    @Operation(
            summary = "Create a new Item",
            description = "Creates a new Item associated with the authenticated user's business"
    )
    @PostMapping
    public ResponseEntity<ItemResponseDto> createItem(@Valid @RequestBody ItemRequestDto request) {
        ItemResponseDto response = itemService.createItem(request);
        return ResponseEntity
                .created(URI.create("/items/" + response.name())) // You might want to use response.id() if available
                .body(response);
    }

    @Operation(
            summary = "Get all Items",
            description = "Returns all Items belonging to the authenticated user's business"
    )
    @GetMapping
    public ResponseEntity<List<ItemResponseDto>> getAllItems() {
        return ResponseEntity.ok(itemService.getAllItems());
    }

    @Operation(
            summary = "Get an Item by ID",
            description = "Retrieves a specific Item by its ID, if owned by the current user's business"
    )
    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDto> getItem(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.getItemById(id));
    }

    @Operation(
            summary = "Update an Item",
            description = "Updates an existing Item by ID, if it belongs to the current user's business"
    )
    @PutMapping("/{id}")
    public ResponseEntity<ItemResponseDto> updateItem(
            @PathVariable Long id,
            @Valid @RequestBody ItemRequestDto request) {
        return ResponseEntity.ok(itemService.updateItem(id, request));
    }

    @Operation(
            summary = "Delete an Item",
            description = "Deletes an Item by ID, if owned by the current user's business"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}
