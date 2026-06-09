package com.example.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción lanzada cuando un usuario autenticado no tiene permisos
 * para acceder a un recurso específico. A diferencia de UnauthorizedException,
 * esta excepción se usa cuando el usuario está autenticado pero no tiene
 * el rol o permiso necesario.
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    private final String requiredRole;
    private final String currentRole;

    /**
     * Constructor de la excepción con información de roles.
     *
     * @param requiredRole Rol requerido para acceder al recurso
     * @param currentRole Rol actual del usuario
     */
    public ForbiddenException(String requiredRole, String currentRole) {
        super(String.format("Acceso denegado. Se requiere rol '%s' pero tiene rol '%s'", requiredRole, currentRole));
        this.requiredRole = requiredRole;
        this.currentRole = currentRole;
    }

    /**
     * Constructor simplificado de la excepción.
     *
     * @param message Mensaje descriptivo del error
     */
    public ForbiddenException(String message) {
        super(message);
        this.requiredRole = null;
        this.currentRole = null;
    }

    /**
     * Obtiene el rol requerido.
     *
     * @return Rol requerido
     */
    public String getRequiredRole() {
        return requiredRole;
    }

    /**
     * Obtiene el rol actual del usuario.
     *
     * @return Rol actual
     */
    public String getCurrentRole() {
        return currentRole;
    }
}