package com.bezkoder.spring.inventory.controller;

import com.bezkoder.spring.inventory.dto.request.ItemExitRequestDto;
import com.bezkoder.spring.inventory.dto.response.ItemExitResponseDto;
import com.bezkoder.spring.inventory.service.ItemExitService;
import com.bezkoder.spring.inventory.util.BusinessContextService;
import com.bezkoder.spring.inventory.util.UserContextService;
import com.bezkoder.spring.security.jwt.models.Business;
import com.bezkoder.spring.security.jwt.models.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/item-exits")
@RequiredArgsConstructor
public class ItemExitController {

    private final ItemExitService itemExitService;
    private final BusinessContextService businessContextService;
    private final UserContextService userContextService;

    @Operation(summary = "Register a new item exit")
    @PostMapping
    public ResponseEntity<ItemExitResponseDto> create(@RequestBody ItemExitRequestDto dto) {
        Business business = businessContextService.getCurrentBusiness();
        User user = userContextService.getCurrentUser();
        return ResponseEntity.ok(itemExitService.create(dto, business, user));
    }

    @Operation(summary = "List or filter item exits")
    @GetMapping
    public ResponseEntity<Page<ItemExitResponseDto>> findAll(
            @Parameter(description = "Filter by item name", example = "Paracetamol") @RequestParam(required = false) String itemName,
            @Parameter(description = "Filter by user name", example = "admin") @RequestParam(required = false) String userName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Business business = businessContextService.getCurrentBusiness();
        Page<ItemExitResponseDto> results = itemExitService.findAllFiltered(itemName, userName, business, PageRequest.of(page, size));
        return ResponseEntity.ok(results);
    }

    @Operation(summary = "Find item exit by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ItemExitResponseDto> findById(@PathVariable Long id) {
        Business business = businessContextService.getCurrentBusiness();
        return ResponseEntity.ok(itemExitService.findById(id, business));
    }

    @Operation(summary = "Delete an item exit by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Business business = businessContextService.getCurrentBusiness();
        itemExitService.deleteById(id, business);
        return ResponseEntity.noContent().build();
    }
}
