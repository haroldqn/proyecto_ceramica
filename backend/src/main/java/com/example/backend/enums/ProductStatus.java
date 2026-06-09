package com.example.backend.enums;

/**
 * Enumeración que representa los diferentes estados de un producto.
 * Cada estado indica la disponibilidad del producto para la venta.
 */
public enum ProductStatus {
    
    /**
     * El producto está disponible para la venta.
     */
    ACTIVE("Activo"),
    
    /**
     * El producto no está disponible temporalmente.
     */
    INACTIVE("Inactivo"),
    
    /**
     * El producto está agotado pero se espera reposición.
     */
    OUT_OF_STOCK("Agotado"),
    
    /**
     * El producto ha sido descontinuado permanentemente.
     */
    DISCONTINUED("Descontinuado"),
    
    /**
     * El producto está en proceso de creación/borrador.
     */
    DRAFT("Borrador"),
    
    /**
     * El producto está pendiente de aprobación.
     */
    PENDING_APPROVAL("Pendiente de aprobación"),
    
    /**
     * El producto está en oferta/promoción.
     */
    ON_SALE("En oferta"),
    
    /**
     * El producto es de edición limitada.
     */
    LIMITED_EDITION("Edición limitada"),
    
    /**
     * El producto es de preventa.
     */
    PRE_ORDER("Preventa");

    private final String description;

    ProductStatus(String description) {
        this.description = description;
    }

    /**
     * Obtiene la descripción legible del estado del producto.
     *
     * @return Descripción del estado
     */
    public String getDescription() {
        return description;
    }

    /**
     * Verifica si el producto está disponible para la compra.
     *
     * @return true si el producto está disponible, false en caso contrario
     */
    public boolean isAvailable() {
        return this == ACTIVE || this == ON_SALE || this == LIMITED_EDITION || this == PRE_ORDER;
    }

    /**
     * Verifica si el producto está visible en el catálogo.
     *
     * @return true si el producto es visible, false en caso contrario
     */
    public boolean isVisible() {
        return this == ACTIVE || this == OUT_OF_STOCK || this == ON_SALE || 
               this == LIMITED_EDITION || this == PRE_ORDER;
    }

    /**
     * Verifica si el producto requiere aprobación administrativa.
     *
     * @return true si requiere aprobación, false en caso contrario
     */
    public boolean requiresApproval() {
        return this == DRAFT || this == PENDING_APPROVAL;
    }
}