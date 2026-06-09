package com.example.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción lanzada cuando un usuario no está autorizado para realizar una acción.
 * Se utiliza para manejar casos en los que las credenciales son inválidas,
 * el token ha expirado o el usuario no tiene permisos suficientes.
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    private final String reason;

    /**
     * Constructor de la excepción con motivo específico.
     *
     * @param reason Motivo de la falta de autorización
     */
    public UnauthorizedException(String reason) {
        super(reason);
        this.reason = reason;
    }

    /**
     * Constructor simplificado de la excepción.
     */
    public UnauthorizedException() {
        super("No autorizado");
        this.reason = "No autorizado";
    }

    /**
     * Obtiene el motivo de la falta de autorización.
     *
     * @return Motivo
     */
    public String getReason() {
        return reason;
    }
}