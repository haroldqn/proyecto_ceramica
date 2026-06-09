package com.example.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción lanzada cuando se realiza una solicitud incorrecta o inválida.
 * Se utiliza para manejar errores de validación de datos de entrada,
 * formatos incorrectos o parámetros inválidos.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    private final String field;
    private final String rejectedValue;

    /**
     * Constructor de la excepción con información del campo inválido.
     *
     * @param field Nombre del campo que causó el error
     * @param rejectedValue Valor rechazado
     * @param message Mensaje descriptivo del error
     */
    public BadRequestException(String field, String rejectedValue, String message) {
        super(message);
        this.field = field;
        this.rejectedValue = rejectedValue;
    }

    /**
     * Constructor simplificado de la excepción.
     *
     * @param message Mensaje descriptivo del error
     */
    public BadRequestException(String message) {
        super(message);
        this.field = null;
        this.rejectedValue = null;
    }

    /**
     * Obtiene el nombre del campo inválido.
     *
     * @return Nombre del campo
     */
    public String getField() {
        return field;
    }

    /**
     * Obtiene el valor rechazado.
     *
     * @return Valor rechazado
     */
    public String getRejectedValue() {
        return rejectedValue;
    }
}