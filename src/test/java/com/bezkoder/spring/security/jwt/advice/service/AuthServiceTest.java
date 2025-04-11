package com.bezkoder.spring.security.jwt.advice.service;

import com.bezkoder.spring.inventory.exception.BusinessNotFoundException;
import com.bezkoder.spring.security.jwt.exception.TokenRefreshException;
import com.bezkoder.spring.security.jwt.models.*;
import com.bezkoder.spring.security.jwt.payload.request.LoginRequest;
import com.bezkoder.spring.security.jwt.payload.request.SignupRequest;
import com.bezkoder.spring.security.jwt.repository.ActionLogRepository;
import com.bezkoder.spring.security.jwt.repository.BusinessRepository;
import com.bezkoder.spring.security.jwt.repository.RoleRepository;
import com.bezkoder.spring.security.jwt.repository.UserRepository;
import com.bezkoder.spring.security.jwt.security.jwt.JwtUtils;
import com.bezkoder.spring.security.jwt.security.services.AuthService;
import com.bezkoder.spring.security.jwt.security.services.RefreshTokenService;
import com.bezkoder.spring.security.jwt.security.services.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock private AuthenticationManager authenticationManager;
    @Mock private UserRepository userRepository;
    @Mock private BusinessRepository businessRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private PasswordEncoder encoder;
    @Mock private JwtUtils jwtUtils;
    @Mock private RefreshTokenService refreshTokenService;
    @Mock private ActionLogRepository actionLogRepository;
    @Mock private HttpServletRequest request;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testAuthenticateUser_Success() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("test");
        loginRequest.setPassword("pass");

        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "test", "test@example.com", "encodedPass", new ArrayList<>());
        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(userDetails);
        when(authenticationManager.authenticate(any())).thenReturn(auth);

        ResponseCookie jwt = ResponseCookie.from("jwt", "token").build();
        ResponseCookie refresh = ResponseCookie.from("refresh", "token").build();

        when(jwtUtils.generateJwtCookie(userDetails)).thenReturn(jwt);
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("token");
        when(refreshTokenService.createRefreshToken(1L)).thenReturn(refreshToken);
        when(jwtUtils.generateRefreshJwtCookie("token")).thenReturn(refresh);

        SecurityContextHolder.getContext().setAuthentication(auth);

        ResponseEntity<?> response = authService.authenticateUser(loginRequest);
        assertEquals(200, response.getStatusCodeValue());
        verify(actionLogRepository, times(1)).save(any());
    }

    @Test
    public void testRegisterUser_UsernameExists() {
        SignupRequest request = new SignupRequest();
        request.setUsername("user");
        request.setEmail("email@example.com");

        when(userRepository.existsByUsername("user")).thenReturn(true);

        ResponseEntity<?> response = authService.registerUser(request);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void testRegisterUser_EmailExists() {
        SignupRequest request = new SignupRequest();
        request.setUsername("user");
        request.setEmail("email@example.com");

        when(userRepository.existsByUsername("user")).thenReturn(false);
        when(userRepository.existsByEmail("email@example.com")).thenReturn(true);

        ResponseEntity<?> response = authService.registerUser(request);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test(expected = BusinessNotFoundException.class)
    public void testRegisterUser_BusinessNotFound() {
        SignupRequest request = new SignupRequest();
        request.setUsername("user");
        request.setEmail("email@example.com");
        request.setBusinessId(1L);

        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(businessRepository.findById(1L)).thenReturn(Optional.empty());

        authService.registerUser(request);
    }

    @Test
    public void testRegisterUser_Success() {
        SignupRequest request = new SignupRequest();
        request.setUsername("user");
        request.setEmail("email@example.com");
        request.setPassword("pass");
        request.setBusinessId(1L);

        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(businessRepository.findById(anyLong())).thenReturn(Optional.of(new Business()));
        when(roleRepository.findByName(any())).thenReturn(Optional.of(new Role()));
        when(encoder.encode(anyString())).thenReturn("encodedPass");

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("admin", null, new ArrayList<>())
        );

        ResponseEntity<?> response = authService.registerUser(request);
        assertEquals(200, response.getStatusCodeValue());
        verify(userRepository, times(1)).save(any());
        verify(actionLogRepository, times(1)).save(any());
    }

    @Test
    public void testLogoutUser_Authenticated() {
        UserDetailsImpl principal = new UserDetailsImpl(1L, "user", "email", "pass", new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(principal, null, new ArrayList<>())
        );

        when(jwtUtils.getCleanJwtCookie()).thenReturn(ResponseCookie.from("jwt", "").build());
        when(jwtUtils.getCleanJwtRefreshCookie()).thenReturn(ResponseCookie.from("refresh", "").build());

        ResponseEntity<?> response = authService.logoutUser();
        assertEquals(200, response.getStatusCodeValue());
        verify(refreshTokenService, times(1)).deleteByUserId(1L);
        verify(actionLogRepository, times(1)).save(any());
    }

    @Test
    public void testLogoutUser_Anonymous() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("anonymousUser", null, new ArrayList<>())
        );

        when(jwtUtils.getCleanJwtCookie()).thenReturn(ResponseCookie.from("jwt", "").build());
        when(jwtUtils.getCleanJwtRefreshCookie()).thenReturn(ResponseCookie.from("refresh", "").build());

        ResponseEntity<?> response = authService.logoutUser();
        assertEquals(200, response.getStatusCodeValue());
        verify(refreshTokenService, never()).deleteByUserId(anyLong());
        verify(actionLogRepository, never()).save(any());
    }

    @Test
    public void testRefreshToken_Success() {
        when(jwtUtils.getJwtRefreshFromCookies(request)).thenReturn("refreshToken");
        RefreshToken token = new RefreshToken();
        token.setUser(new User());

        when(refreshTokenService.findByToken("refreshToken")).thenReturn(Optional.of(token));
        when(refreshTokenService.verifyExpiration(token)).thenReturn(token);
        when(jwtUtils.generateJwtCookie(token.getUser()))
                .thenReturn(ResponseCookie.from("jwt", "newToken").build());

        ResponseEntity<?> response = authService.refreshToken(request);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testRefreshToken_EmptyToken() {
        when(jwtUtils.getJwtRefreshFromCookies(request)).thenReturn("");

        ResponseEntity<?> response = authService.refreshToken(request);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test(expected = TokenRefreshException.class)
    public void testRefreshToken_NotInDatabase() {
        when(jwtUtils.getJwtRefreshFromCookies(request)).thenReturn("invalidToken");
        when(refreshTokenService.findByToken("invalidToken")).thenReturn(Optional.empty());

        authService.refreshToken(request);
    }
}
