package com.example.backend.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Utilidad para generar números de orden únicos.
 */
public final class OrderNumberGenerator {
    
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
    
    private OrderNumberGenerator() {
        // Clase utilitaria, no instanciable
    }
    
    /**
     * Genera un número de orden único basado en la fecha actual.
     * Formato: ORD-YYYYMMDD-XXXXX
     * 
     * @param sequence Número secuencial del día
     * @return Número de orden generado
     */
    public static String generate(int sequence) {
        String dateStr = LocalDate.now().format(DATE_FORMAT);
        String seqStr = String.format("%05d", sequence);
        return "ORD-" + dateStr + "-" + seqStr;
    }
    
    /**
     * Genera un número de orden único basado en un ID.
     * Formato: ORD-XXXXXX
     * 
     * @param id ID de la orden
     * @return Número de orden generado
     */
    public static String generateFromId(int id) {
        return "ORD-" + String.format("%06d", id);
    }
    
    /**
     * Genera un número de transacción único.
     * Formato: TXN-XXXXXXXX
     * 
     * @param randomPart Parte aleatoria de 8 caracteres
     * @return Número de transacción generado
     */
    public static String generateTransactionId(String randomPart) {
        return "TXN-" + randomPart.toUpperCase();
    }
}