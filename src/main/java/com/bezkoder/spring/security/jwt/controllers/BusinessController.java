package com.bezkoder.spring.security.jwt.controller;

import com.bezkoder.spring.security.jwt.payload.request.BusinessRequestDto;
import com.bezkoder.spring.security.jwt.payload.response.BusinessResponseDto;
import com.bezkoder.spring.security.jwt.payload.request.BusinessFilterDto;
import com.bezkoder.spring.security.jwt.service.BusinessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/businesses")
@RequiredArgsConstructor
public class BusinessController {

    private final BusinessService businessService;

    @Operation(summary = "Create a new business")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Business created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or duplicate name")
    })

    @PostMapping
    public ResponseEntity<BusinessResponseDto> createBusiness(@RequestBody BusinessRequestDto dto) {
        return ResponseEntity.ok(businessService.createBusiness(dto));
    }

    @Operation(summary = "Get a business by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Business retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Business not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BusinessResponseDto> getBusinessById(@PathVariable Long id) {
        return ResponseEntity.ok(businessService.getBusinessById(id));
    }

    @Operation(summary = "Filter or list all businesses with pagination by name, industry, city, or state")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Businesses retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<Page<BusinessResponseDto>> filterBusinesses(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String industry,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String state,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        BusinessFilterDto filter = new BusinessFilterDto(name, industry, city, state);
        return ResponseEntity.ok(businessService.filterBusinesses(filter, page, size));
    }

    @Operation(summary = "Update an existing business by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Business updated successfully"),
            @ApiResponse(responseCode = "404", description = "Business not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<BusinessResponseDto> updateBusiness(
            @PathVariable Long id,
            @RequestBody BusinessRequestDto dto
    ) {
        return ResponseEntity.ok(businessService.updateBusiness(id, dto));
    }

    @Operation(summary = "Delete a business by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Business deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Business not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBusiness(@PathVariable Long id) {
        businessService.deleteBusiness(id);
        return ResponseEntity.noContent().build();
    }
}
