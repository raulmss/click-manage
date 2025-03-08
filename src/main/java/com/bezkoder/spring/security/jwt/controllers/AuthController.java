package com.bezkoder.spring.security.jwt.controllers;

import com.bezkoder.spring.security.jwt.payload.request.LoginRequest;
import com.bezkoder.spring.security.jwt.payload.request.SignupRequest;
import com.bezkoder.spring.security.jwt.security.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  private AuthService authService;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    return authService.authenticateUser(loginRequest);
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    return authService.registerUser(signUpRequest);
  }

  @PostMapping("/signout")
  public ResponseEntity<?> logoutUser() {
    return authService.logoutUser();
  }

  @PostMapping("/refreshtoken")
  public ResponseEntity<?> refreshToken(HttpServletRequest request) {
    return authService.refreshToken(request);
  }
}
