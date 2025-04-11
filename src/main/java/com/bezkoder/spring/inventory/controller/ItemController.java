package com.bezkoder.spring.inventory.controller;

import com.bezkoder.spring.inventory.service.ItemService;
import com.bezkoder.spring.inventory.dto.request.ItemRequestDto;
import com.bezkoder.spring.inventory.dto.response.ItemResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemResponseDto> createItem(@Valid @RequestBody ItemRequestDto request) {
        return ResponseEntity.ok(itemService.createItem(request));
    }

    @GetMapping
    public ResponseEntity<List<ItemResponseDto>> getAllItems() {
        return ResponseEntity.ok(itemService.getAllItems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDto> getItem(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.getItemById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemResponseDto> updateItem(@PathVariable Long id, @Valid @RequestBody ItemRequestDto request) {
        return ResponseEntity.ok(itemService.updateItem(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}
