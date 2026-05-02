package com.example.backend.dto;

public record RegisterRequest(
        String dni,
        String firstName,
        String lastName,
        String motherLastName,
        String birthDate,
        String email,
        String password
) {
}
