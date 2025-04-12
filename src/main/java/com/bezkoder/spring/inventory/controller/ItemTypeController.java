package com.bezkoder.spring.inventory.controller;

import com.bezkoder.spring.inventory.dto.request.ItemTypeRequestDto;
import com.bezkoder.spring.inventory.dto.response.ItemTypeResponseDto;
import com.bezkoder.spring.inventory.service.ItemTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/item-types")
@RequiredArgsConstructor
@Tag(name = "Item Types", description = "Operations related to item types")
public class ItemTypeController {

    private final ItemTypeService itemTypeService;

    @Operation(
            summary = "Create a new ItemType",
            description = "Creates a new ItemType for the authenticated user's business"
    )
    @PostMapping
    public ResponseEntity<ItemTypeResponseDto> create(@Valid @RequestBody ItemTypeRequestDto request) {
        ItemTypeResponseDto response = itemTypeService.createType(request);
        return ResponseEntity
                .created(URI.create("/item-types/" + response.id()))
                .body(response);
    }

    @Operation(
            summary = "Get all ItemTypes",
            description = "Returns all ItemTypes associated with the authenticated user's business"
    )
    @GetMapping
    public ResponseEntity<List<ItemTypeResponseDto>> getAll() {
        return ResponseEntity.ok(itemTypeService.getAllTypes());
    }

    @Operation(
            summary = "Delete an ItemType by ID",
            description = "Deletes the specified ItemType if it belongs to the authenticated user's business"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        itemTypeService.deleteType(id);
        return ResponseEntity.noContent().build();
    }
}
