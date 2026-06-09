package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para la respuesta de un procesamiento de pago.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
    
    private Long id;
    
    private Long orderId;
    
    private String orderNumber;
    
    private String paymentMethod;
    
    private String status;
    
    private BigDecimal amount;
    
    private LocalDateTime paymentDate;
    
    private String transactionId;
    
    private String authorizationCode;
    
    private String errorMessage;
    
    private LocalDateTime createdAt;
}