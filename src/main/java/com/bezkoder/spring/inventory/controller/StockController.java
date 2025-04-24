package com.bezkoder.spring.inventory.controller;

import com.bezkoder.spring.inventory.dto.request.StockRequestDto;
import com.bezkoder.spring.inventory.dto.response.StockResponseDto;
import com.bezkoder.spring.inventory.service.StockService;
import com.bezkoder.spring.inventory.util.BusinessContextService;
import com.bezkoder.spring.security.jwt.models.Business;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;
    private final BusinessContextService businessContextService;

    @Operation(summary = "List or filter stocks", description = "Get paginated stock data filtered by item name and/or item type name. Supports sorting.")
    @GetMapping
    public ResponseEntity<Page<StockResponseDto>> getStocks(
            @Parameter(description = "Filter by item name (optional)") @RequestParam(required = false) String itemName,
            @Parameter(description = "Filter by item type name (optional)") @RequestParam(required = false) String itemTypeName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort direction: ASC or DESC (default is ASC)") @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        Business business = businessContextService.getCurrentBusiness();
        return ResponseEntity.ok(
                stockService.findAllFiltered(business, itemName, itemTypeName, page, size, sortDirection)
        );
    }

    @Operation(summary = "Get stock by ID")
    @GetMapping("/{id}")
    public ResponseEntity<StockResponseDto> getById(@PathVariable Long id) {
        Business business = businessContextService.getCurrentBusiness();
        return ResponseEntity.ok(stockService.getStockById(id, business));
    }

    @Operation(summary = "Update stock minimum threshold")
    @PutMapping("/{id}")
    public ResponseEntity<StockResponseDto> updateStock(@PathVariable Long id, @RequestBody StockRequestDto dto) {
        Business business = businessContextService.getCurrentBusiness();
        return ResponseEntity.ok(stockService.updateStock(id, dto, business));
    }

    @Operation(summary = "Delete stock by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Business business = businessContextService.getCurrentBusiness();
        stockService.deleteStock(id, business);
        return ResponseEntity.noContent().build();
    }
}
