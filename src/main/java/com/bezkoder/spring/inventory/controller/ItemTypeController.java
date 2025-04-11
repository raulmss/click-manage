package com.bezkoder.spring.inventory.controller;


import com.bezkoder.spring.inventory.service.ItemTypeService;
import com.bezkoder.spring.inventory.dto.request.ItemTypeRequestDto;
import com.bezkoder.spring.inventory.dto.response.ItemTypeResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/item-types")
@RequiredArgsConstructor
public class ItemTypeController {

    private final ItemTypeService itemTypeService;

    @PostMapping
    public ResponseEntity<ItemTypeResponseDto> create(@Valid @RequestBody ItemTypeRequestDto request) {
        return ResponseEntity.ok(itemTypeService.createType(request));
    }

    @GetMapping
    public ResponseEntity<List<ItemTypeResponseDto>> getAll() {
        return ResponseEntity.ok(itemTypeService.getAllTypes());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        itemTypeService.deleteType(id);
        return ResponseEntity.noContent().build();
    }
}
