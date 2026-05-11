package com.example.backend.services;

import com.example.backend.dto.GoogleLoginRequest;
import com.example.backend.dto.GoogleTokenInfoResponse;
import com.example.backend.dto.LoginRequest;
import com.example.backend.dto.LoginResponse;
import com.example.backend.dto.RegisterRequest;
import com.example.backend.models.Persona;
import com.example.backend.models.Role;
import com.example.backend.models.User;
import com.example.backend.repositories.PersonaRepository;
import com.example.backend.repositories.RoleRepository;
import com.example.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PersonaRepository personaRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final GoogleAuthService googleAuthService;

    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        var user = userRepository.findByEmail(request.email()).orElseThrow();
        String token = jwtService.generateToken(userDetails);

        return new LoginResponse(token, user.getPersona().getName(), user.getRole().getName());
    }

    public LoginResponse loginWithGoogle(GoogleLoginRequest request) {
        GoogleTokenInfoResponse tokenInfo = googleAuthService.verifyCredential(request.credential());

        User user = userRepository.findByGoogleId(tokenInfo.subject())
                .orElseGet(() -> userRepository.findByEmail(tokenInfo.email())
                        .map(existingUser -> attachGoogleIdentity(existingUser, tokenInfo))
                        .orElseGet(() -> createGoogleUser(tokenInfo)));

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().getName())
                .build();

        String token = jwtService.generateToken(userDetails);
        return new LoginResponse(token, user.getPersona().getName(), user.getRole().getName());
    }

    public void register(RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("El email ya existe");
        }

        if (isBlank(request.dni()) || isBlank(request.firstName()) || isBlank(request.lastName())
                || isBlank(request.motherLastName())) {
            throw new RuntimeException("Debes consultar y completar los datos del DNI antes de registrarte");
        }

        Role role = roleRepository.findByName("CLIENTE")
                .orElseThrow(() -> new RuntimeException("Role CLIENTE not found"));

        User user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setAuthProvider("LOCAL");
        user.setRole(role);

        Persona persona = new Persona();
        persona.setDni(request.dni());
        persona.setFirstName(request.firstName());
        persona.setLastName(request.lastName());
        persona.setMotherLastName(request.motherLastName());
        persona.setBirthDate(request.birthDate());
        persona.setName(buildFullName(request));
        persona = personaRepository.save(persona);

        user.setPersona(persona);
        userRepository.save(user);
    }

    private User attachGoogleIdentity(User user, GoogleTokenInfoResponse tokenInfo) {
        user.setGoogleId(tokenInfo.subject());
        user.setAuthProvider("GOOGLE");
        return userRepository.save(user);
    }

    private User createGoogleUser(GoogleTokenInfoResponse tokenInfo) {
        Role role = roleRepository.findByName("CLIENTE")
                .orElseThrow(() -> new RuntimeException("Role CLIENTE not found"));

        Persona persona = new Persona();
        persona.setName(resolveDisplayName(tokenInfo));
        persona.setFirstName(resolveFirstName(tokenInfo));
        persona.setLastName(resolveLastName(tokenInfo));
        persona.setMotherLastName("");
        persona.setBirthDate(null);
        persona = personaRepository.save(persona);

        User user = new User();
        user.setEmail(tokenInfo.email());
        user.setPassword(passwordEncoder.encode("GOOGLE-" + tokenInfo.subject()));
        user.setGoogleId(tokenInfo.subject());
        user.setAuthProvider("GOOGLE");
        user.setRole(role);
        user.setPersona(persona);

        return userRepository.save(user);
    }

    private String buildFullName(RegisterRequest request) {
        return String.join(" ",
                request.firstName().trim(),
                request.lastName().trim(),
                request.motherLastName().trim()
        ).trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private String resolveDisplayName(GoogleTokenInfoResponse tokenInfo) {
        if (!isBlank(tokenInfo.name())) {
            return tokenInfo.name().trim();
        }
        return String.join(" ", resolveFirstName(tokenInfo), resolveLastName(tokenInfo)).trim();
    }

    private String resolveFirstName(GoogleTokenInfoResponse tokenInfo) {
        if (!isBlank(tokenInfo.givenName())) {
            return tokenInfo.givenName().trim();
        }
        return !isBlank(tokenInfo.name()) ? tokenInfo.name().trim() : "Usuario";
    }

    private String resolveLastName(GoogleTokenInfoResponse tokenInfo) {
        if (!isBlank(tokenInfo.familyName())) {
            return tokenInfo.familyName().trim();
        }
        return "";
    }
}
