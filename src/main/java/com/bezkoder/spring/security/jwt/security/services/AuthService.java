// Update AuthService.java
package com.bezkoder.spring.security.jwt.security.services;

import com.bezkoder.spring.inventory.exception.BusinessNotFoundException;
import com.bezkoder.spring.inventory.model.Business;
import com.bezkoder.spring.security.jwt.exception.TokenRefreshException;
import com.bezkoder.spring.security.jwt.models.*;
import com.bezkoder.spring.security.jwt.payload.request.LoginRequest;
import com.bezkoder.spring.security.jwt.payload.request.SignupRequest;
import com.bezkoder.spring.security.jwt.payload.response.MessageResponse;
import com.bezkoder.spring.security.jwt.payload.response.UserInfoResponse;
import com.bezkoder.spring.security.jwt.repository.ActionLogRepository;
import com.bezkoder.spring.security.jwt.repository.BusinessRepository;
import com.bezkoder.spring.security.jwt.repository.RoleRepository;
import com.bezkoder.spring.security.jwt.repository.UserRepository;
import com.bezkoder.spring.security.jwt.security.jwt.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private ActionLogRepository actionLogRepository;

    private void logAction(String username, String action, String requestInfo) {
        ActionLog log = new ActionLog();
        log.setUsername(username);
        log.setAction(action);
        log.setTimestamp(LocalDateTime.now());
        log.setRequestInfo(requestInfo);
        actionLogRepository.save(log);
    }

    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        ResponseCookie jwtRefreshCookie = jwtUtils.generateRefreshJwtCookie(refreshToken.getToken());

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        logAction(loginRequest.getUsername(), "Sign In", "Username: " + loggedInUsername);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                .body(new UserInfoResponse(
                        userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        roles,
                        jwtCookie.toString(),
                        jwtRefreshCookie.toString()));
    }

    private ResponseEntity<?> registerUserWithRole(SignupRequest signUpRequest, ERole role) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        Business business = businessRepository.findById(signUpRequest.getBusinessId())
                .orElseThrow(() -> new BusinessNotFoundException("Business ID: " + signUpRequest.getBusinessId()));

        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                business);

        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(role)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found.")));

        user.setRoles(roles);
        userRepository.save(user);

        String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        logAction(loggedInUsername, "Account Creation", "Username: " + signUpRequest.getUsername() + ", Email: " + signUpRequest.getEmail());

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    public ResponseEntity<?> registerUser(SignupRequest signUpRequest) {
        return registerUserWithRole(signUpRequest, ERole.ROLE_USER);
    }

    public ResponseEntity<?> registerModerator(SignupRequest signUpRequest) {
        return registerUserWithRole(signUpRequest, ERole.ROLE_MODERATOR);
    }

    public ResponseEntity<?> registerAdmin(SignupRequest signUpRequest) {
        return registerUserWithRole(signUpRequest, ERole.ROLE_ADMIN);
    }

    public ResponseEntity<?> logoutUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!principal.toString().equals("anonymousUser")) {
            Long userId = ((UserDetailsImpl) principal).getId();
            refreshTokenService.deleteByUserId(userId);
            logAction(((UserDetailsImpl) principal).getUsername(), "Sign Out", "User signed out");
        }

        ResponseCookie jwtCookie = jwtUtils.getCleanJwtCookie();
        ResponseCookie jwtRefreshCookie = jwtUtils.getCleanJwtRefreshCookie();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }

    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String refreshToken = jwtUtils.getJwtRefreshFromCookies(request);

        if (refreshToken != null && !refreshToken.isEmpty()) {
            return refreshTokenService.findByToken(refreshToken)
                    .map(refreshTokenService::verifyExpiration)
                    .map(RefreshToken::getUser)
                    .map(user -> {
                        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(user);
                        return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                                .body(new MessageResponse("Token is refreshed successfully!"));
                    })
                    .orElseThrow(() -> new TokenRefreshException(refreshToken, "Refresh token is not in database!"));
        }

        return ResponseEntity.badRequest().body(new MessageResponse("Refresh Token is empty!"));
    }
}