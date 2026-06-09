package com.example.backend.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Utilidad para cálculos de precios y totales.
 */
public final class PriceCalculator {
    
    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    
    private PriceCalculator() {
        // Clase utilitaria, no instanciable
    }
    
    /**
     * Calcula el subtotal de un item.
     * 
     * @param unitPrice Precio unitario
     * @param quantity Cantidad
     * @return Subtotal
     */
    public static BigDecimal calculateSubtotal(BigDecimal unitPrice, int quantity) {
        return unitPrice.multiply(BigDecimal.valueOf(quantity))
                .setScale(SCALE, ROUNDING_MODE);
    }
    
    /**
     * Calcula el total de una orden con múltiples items.
     * 
     * @param subtotals Lista de subtotales
     * @return Total
     */
    public static BigDecimal calculateTotal(BigDecimal... subtotals) {
        BigDecimal total = BigDecimal.ZERO;
        for (BigDecimal subtotal : subtotals) {
            total = total.add(subtotal).setScale(SCALE, ROUNDING_MODE);
        }
        return total;
    }
    
    /**
     * Aplica un descuento porcentual a un monto.
     * 
     * @param amount Monto original
     * @param discountPercentage Porcentaje de descuento (0-100)
     * @return Monto con descuento
     */
    public static BigDecimal applyDiscount(BigDecimal amount, BigDecimal discountPercentage) {
        BigDecimal discount = amount.multiply(discountPercentage)
                .divide(BigDecimal.valueOf(100), SCALE, ROUNDING_MODE);
        return amount.subtract(discount).setScale(SCALE, ROUNDING_MODE);
    }
    
    /**
     * Calcula el IGV (18%) de un monto.
     * 
     * @param amount Monto base
     * @return IGV
     */
    public static BigDecimal calculateIGV(BigDecimal amount) {
        return amount.multiply(BigDecimal.valueOf(0.18))
                .setScale(SCALE, ROUNDING_MODE);
    }
    
    /**
     * Calcula el total con IGV incluido.
     * 
     * @param amount Monto base
     * @return Total con IGV
     */
    public static BigDecimal calculateTotalWithIGV(BigDecimal amount) {
        return amount.multiply(BigDecimal.valueOf(1.18))
                .setScale(SCALE, ROUNDING_MODE);
    }
    
    /**
     * Obtiene el monto base de un total con IGV incluido.
     * 
     * @param totalWithIGV Total con IGV
     * @return Monto base
     */
    public static BigDecimal getBaseAmount(BigDecimal totalWithIGV) {
        return totalWithIGV.divide(BigDecimal.valueOf(1.18), SCALE, ROUNDING_MODE);
    }
}