package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para actualizar el estado de una orden.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderStatusRequest {
    
    private String status;
    
    private String trackingNumber;
    
    private String observation;
    
    private Boolean notifyClient;
}