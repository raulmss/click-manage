package com.bezkoder.spring.security.jwt.controllers;

import com.bezkoder.spring.inventory.util.BusinessContextService;
import com.bezkoder.spring.security.jwt.models.Business;
import com.bezkoder.spring.security.jwt.payload.response.UserResponseDto;
import com.bezkoder.spring.security.jwt.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final BusinessContextService businessContextService;

    @Operation(summary = "Filter or list all users according to user's business")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERADMIN')")
    public ResponseEntity<Page<UserResponseDto>> getAllFiltered(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "ASC") String sortDir,
            @RequestParam(defaultValue = "username") String sortField
    ) {
        Business business = businessContextService.getCurrentBusiness();
        return ResponseEntity.ok(
                userService.findAllFiltered(username, email, role, business, page, size, sortDir, sortField)
        );
    }

    @Operation(
            summary = "Admin - Filter all users",
            description = "Allows system administrators to filter users by username, email, role, and business name across all businesses. Supports pagination and sorting."
    )
    @GetMapping("/admin")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Page<UserResponseDto>> filterAllUsersForAdmin(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String businessName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Page<UserResponseDto> result = userService.filterAllUsersForAdmin(
                username,
                email,
                role,
                businessName,
                page,
                size,
                sortDir
        );
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Find user by ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERADMIN')")
    public ResponseEntity<UserResponseDto> findById(@PathVariable Long id) {
        Business business = businessContextService.getCurrentBusiness();
        return ResponseEntity.ok(userService.findById(id, business));
    }

    @Operation(summary = "Find user by username")
    @GetMapping("/by-username/{username}")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<UserResponseDto> findByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.findByUsername(username));
    }

    @Operation(summary = "Delete user by username for the current business")
    @DeleteMapping("/{username}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERADMIN')")
    public ResponseEntity<Void> deleteByUsername(@PathVariable String username) {
        Business business = businessContextService.getCurrentBusiness();
        userService.deleteByUsername(username, business);
        return ResponseEntity.noContent().build();
    }
    
    //add another one where super admin can erase any regardless of business.
}

