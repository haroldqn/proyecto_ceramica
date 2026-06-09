package com.example.backend.services;

import com.example.backend.dto.LoginRequest;
import com.example.backend.dto.LoginResponse;
import com.example.backend.dto.RegisterRequest;
import com.example.backend.models.Persona;
import com.example.backend.models.Role;
import com.example.backend.models.User;
import com.example.backend.repositories.PersonaRepository;
import com.example.backend.repositories.RoleRepository;
import com.example.backend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PersonaRepository personaRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    private User testUser;
    private Role testRole;
    private Persona testPersona;

    @BeforeEach
    void setUp() {
        testRole = new Role();
        testRole.setId(2L);
        testRole.setName("CLIENTE");

        testPersona = new Persona();
        testPersona.setId(1L);
        testPersona.setName("Test User");
        testPersona.setFirstName("Test");
        testPersona.setLastName("User");
        testPersona.setDni("12345678");
        testPersona.setBirthDate("1990-01-01");

        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setPassword("encodedPassword");
        testUser.setRole(testRole);
        testUser.setPersona(testPersona);
        testUser.setAuthProvider("LOCAL");
    }

    @Test
    void register_WithValidRequest_ReturnsLoginResponse() {
        // Given
        RegisterRequest request = new RegisterRequest(
                "87654321", "New", "User", "", "1995-05-15",
                "newuser@example.com", "password123"
        );

        when(roleRepository.findByName("CLIENTE")).thenReturn(Optional.of(testRole));
        when(personaRepository.findByDni(request.dni())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.password())).thenReturn("encodedPassword");
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("test-jwt-token");

        // When
        LoginResponse response = authService.register(request);

        // Then
        assertNotNull(response);
        assertEquals("test-jwt-token", response.token());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_WhenRoleNotFound_ThrowsRuntimeException() {
        // Given
        RegisterRequest request = new RegisterRequest(
                "87654321", "New", "User", "", "1995-05-15",
                "newuser@example.com", "password123"
        );

        when(roleRepository.findByName("CLIENTE")).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> authService.register(request)
        );
        assertEquals("Role CLIENTE not found", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_WhenDniAlreadyExists_ThrowsRuntimeException() {
        // Given
        RegisterRequest request = new RegisterRequest(
                "12345678", "New", "User", "", "1995-05-15",
                "newuser@example.com", "password123"
        );

        when(roleRepository.findByName("CLIENTE")).thenReturn(Optional.of(testRole));
        when(personaRepository.findByDni(request.dni())).thenReturn(Optional.of(testPersona));

        // When & Then
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> authService.register(request)
        );
        assertTrue(exception.getMessage().contains("ya existe") || exception.getMessage().contains("existe"));
        verify(userRepository, never()).save(any());
    }

    @Test
    void login_WithValidCredentials_ReturnsLoginResponse() {
        // Given
        LoginRequest request = new LoginRequest("test@example.com", "password123");

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(request.password(), testUser.getPassword())).thenReturn(true);
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("test-jwt-token");

        // When
        LoginResponse response = authService.login(request);

        // Then
        assertNotNull(response);
        assertEquals("test-jwt-token", response.token());
        assertEquals("Test User", response.name());
        assertEquals("CLIENTE", response.role());
    }

    @Test
    void login_WhenUserNotFound_ThrowsRuntimeException() {
        // Given
        LoginRequest request = new LoginRequest("notfound@example.com", "password123");

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> authService.login(request)
        );
        assertTrue(exception.getMessage().contains("No existe") || exception.getMessage().contains("existe"));
    }

    @Test
    void login_WhenInvalidPassword_ThrowsRuntimeException() {
        // Given
        LoginRequest request = new LoginRequest("test@example.com", "wrongpassword");

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(request.password(), testUser.getPassword())).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> authService.login(request)
        );
        assertTrue(exception.getMessage().contains("contraseña") || exception.getMessage().contains("password"));
    }
}