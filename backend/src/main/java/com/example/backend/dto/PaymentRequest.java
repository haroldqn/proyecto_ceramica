package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para procesar un pago de una orden.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    
    private Long orderId;
    
    private String paymentMethod;
    
    private String cardNumber;
    
    private String cardHolder;
    
    private String expiryDate;
    
    private String cvv;
    
    private BigDecimal amount;
    
    private String transactionId;
    
    private String paymentGateway;
}