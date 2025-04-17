package com.bezkoder.spring.inventory.controller;

import com.bezkoder.spring.inventory.dto.request.ItemRequestDto;
import com.bezkoder.spring.inventory.dto.response.ItemResponseDto;
import com.bezkoder.spring.inventory.service.ItemService;
import com.bezkoder.spring.security.jwt.models.Business;
import com.bezkoder.spring.inventory.util.BusinessContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final BusinessContextService businessContextService;

    @PostMapping
    public ResponseEntity<ItemResponseDto> create(@RequestBody ItemRequestDto dto) {
        Business business = businessContextService.getCurrentBusiness();
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.create(dto, business));
    }

    @GetMapping
    public ResponseEntity<Page<ItemResponseDto>> findAll(Pageable pageable) {
        Business business = businessContextService.getCurrentBusiness();
        return ResponseEntity.ok(itemService.findAllByBusiness(business, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDto> findById(@PathVariable Long id) {
        Business business = businessContextService.getCurrentBusiness();
        return ResponseEntity.ok(itemService.findByIdAndBusiness(id, business));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemResponseDto> update(@PathVariable Long id, @RequestBody ItemRequestDto dto) {
        Business business = businessContextService.getCurrentBusiness();
        return ResponseEntity.ok(itemService.update(id, dto, business));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Business business = businessContextService.getCurrentBusiness();
        itemService.delete(id, business);
        return ResponseEntity.noContent().build();
    }
}