package com.bezkoder.spring.security.jwt.controllers.inventory;


import com.bezkoder.spring.security.jwt.advice.service.inventory.ItemTypeService;
import com.bezkoder.spring.security.jwt.payload.request.inventory.ItemTypeRequestDto;
import com.bezkoder.spring.security.jwt.payload.response.inventory.ItemTypeResponseDto;
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
