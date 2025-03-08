package com.bezkoder.spring.security.jwt.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//for Angular Client (withCredentials)
//@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
@Tag(name = "Test Endpoints", description = "Endpoints for Testing Permissions")
public class TestController {
  @GetMapping("/all")
  @Operation(summary = "Get Test", description = "No need to have any role")
  public String allAccess() {
    return "Public Content.";
  }

  @GetMapping("/user")
  @Operation(summary = "Get Test", description = "Has to have Roles User, Moderator or Admin")
  @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
  public String userAccess() {
    return "User Content.";
  }

  @GetMapping("/mod")
  @Operation(summary = "Get Test", description = "Has to have Role Moderator")
  @PreAuthorize("hasRole('MODERATOR')")
  public String moderatorAccess() {
    return "Moderator Board.";
  }

  @GetMapping("/admin")
  @Operation(summary = "Get Test", description = "Has to have Role Admin")
  @PreAuthorize("hasRole('ADMIN')")
  public String adminAccess() {
    return "Admin Board.";
  }
}
