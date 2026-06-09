package com.example.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción lanzada cuando no se encuentra un recurso solicitado.
 * Se utiliza para manejar casos en los que se solicita un recurso
 * (como un producto, usuario, categoría, etc.) que no existe en la base de datos.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    private final String resourceName;
    private final String fieldName;
    private final Object fieldValue;

    /**
     * Constructor de la excepción con información detallada del recurso.
     *
     * @param resourceName Nombre del recurso (ej: "Producto", "Usuario")
     * @param fieldName Nombre del campo utilizado para la búsqueda
     * @param fieldValue Valor del campo utilizado en la búsqueda
     */
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s no encontrado con %s: '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    /**
     * Constructor simplificado de la excepción.
     *
     * @param message Mensaje descriptivo del error
     */
    public ResourceNotFoundException(String message) {
        super(message);
        this.resourceName = null;
        this.fieldName = null;
        this.fieldValue = null;
    }

    /**
     * Obtiene el nombre del recurso.
     *
     * @return Nombre del recurso
     */
    public String getResourceName() {
        return resourceName;
    }

    /**
     * Obtiene el nombre del campo.
     *
     * @return Nombre del campo
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Obtiene el valor del campo.
     *
     * @return Valor del campo
     */
    public Object getFieldValue() {
        return fieldValue;
    }
}