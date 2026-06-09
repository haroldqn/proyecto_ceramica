package com.example.backend.enums;

/**
 * Enumeración que representa los diferentes métodos de pago disponibles.
 * Cada método tiene una descripción y puede tener configuraciones específicas.
 */
public enum PaymentMethod {
    
    /**
     * Pago con tarjeta de crédito.
     */
    CREDIT_CARD("Tarjeta de Crédito", true),
    
    /**
     * Pago con tarjeta de débito.
     */
    DEBIT_CARD("Tarjeta de Débito", true),
    
    /**
     * Pago con PayPal.
     */
    PAYPAL("PayPal", true),
    
    /**
     * Pago con transferencia bancaria.
     */
    BANK_TRANSFER("Transferencia Bancaria", false),
    
    /**
     * Pago contra entrega (efectivo).
     */
    CASH_ON_DELIVERY("Pago contra entrega", false),
    
    /**
     * Pago con Yape (billetera digital de BCP).
     */
    YAPE("Yape", false),
    
    /**
     * Pago con Plin (billetera digital de Interbank).
     */
    PLIN("Plin", false),
    
    /**
     * Pago con Apple Pay.
     */
    APPLE_PAY("Apple Pay", true),
    
    /**
     * Pago con Google Pay.
     */
    GOOGLE_PAY("Google Pay", true);

    private final String description;
    private final boolean isOnline;

    PaymentMethod(String description, boolean isOnline) {
        this.description = description;
        this.isOnline = isOnline;
    }

    /**
     * Obtiene la descripción legible del método de pago.
     *
     * @return Descripción del método de pago
     */
    public String getDescription() {
        return description;
    }

    /**
     * Verifica si el método de pago es en línea.
     *
     * @return true si es un método de pago en línea, false en caso contrario
     */
    public boolean isOnline() {
        return isOnline;
    }

    /**
     * Verifica si el método de pago requiere procesamiento inmediato.
     *
     * @return true si requiere procesamiento inmediato, false en caso contrario
     */
    public boolean requiresImmediateProcessing() {
        return this == CREDIT_CARD || this == DEBIT_CARD || this == PAYPAL || 
               this == APPLE_PAY || this == GOOGLE_PAY || this == YAPE || this == PLIN;
    }

    /**
     * Verifica si el método de pago es una billetera digital.
     *
     * @return true si es una billetera digital, false en caso contrario
     */
    public boolean isDigitalWallet() {
        return this == YAPE || this == PLIN || this == APPLE_PAY || this == GOOGLE_PAY || this == PAYPAL;
    }
}