package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para resumen de orden.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderSummaryDTO {
    
    private Long id;
    
    private String orderNumber;
    
    private String status;
    
    private LocalDateTime registerDate;
    
    private Integer itemCount;
    
    private Double total;
    
    private String clientName;
}