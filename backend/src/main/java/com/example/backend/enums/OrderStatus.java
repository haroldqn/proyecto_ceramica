package com.example.backend.enums;

/**
 * Enumeración que representa los diferentes estados de un pedido.
 * Cada estado indica la fase actual del proceso de compra y envío.
 */
public enum OrderStatus {
    
    /**
     * El pedido ha sido creado pero no ha sido confirmado.
     */
    PENDING("Pendiente"),
    
    /**
     * El pedido ha sido confirmado y está siendo procesado.
     */
    CONFIRMED("Confirmado"),
    
    /**
     * El pedido está siendo preparado para el envío.
     */
    PROCESSING("Procesando"),
    
    /**
     * El pedido ha sido enviado y está en camino.
     */
    SHIPPED("Enviado"),
    
    /**
     * El pedido ha sido entregado al cliente.
     */
    DELIVERED("Entregado"),
    
    /**
     * El pedido ha sido cancelado por el cliente o el administrador.
     */
    CANCELLED("Cancelado"),
    
    /**
     * El pedido ha sido devuelto por el cliente.
     */
    RETURNED("Devuelto"),
    
    /**
     * El pedido está en proceso de reembolso.
     */
    REFUNDING("Reembolsando"),
    
    /**
     * El pedido ha sido reembolsado completamente.
     */
    REFUNDED("Reembolsado");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    /**
     * Obtiene la descripción legible del estado del pedido.
     *
     * @return Descripción del estado
     */
    public String getDescription() {
        return description;
    }

    /**
     * Verifica si el pedido está en un estado activo (no cancelado ni entregado).
     *
     * @return true si el pedido está activo, false en caso contrario
     */
    public boolean isActive() {
        return this == PENDING || this == CONFIRMED || this == PROCESSING || this == SHIPPED;
    }

    /**
     * Verifica si el pedido ha sido completado (entregado o devuelto).
     *
     * @return true si el pedido está completado, false en caso contrario
     */
    public boolean isCompleted() {
        return this == DELIVERED || this == RETURNED || this == REFUNDED;
    }

    /**
     * Verifica si el pedido puede ser cancelado.
     *
     * @return true si el pedido puede ser cancelado, false en caso contrario
     */
    public boolean canBeCancelled() {
        return this == PENDING || this == CONFIRMED;
    }
}