package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO para crear una nueva orden de compra.
 * Contiene toda la información necesaria para registrar un pedido.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    
    private Long clientId;
    
    private List<OrderItemRequest> items;
    
    private String paymentMethod;
    
    private String shippingAddress;
    
    private String additionalNotes;
    
    /**
     * DTO para los items de una orden
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderItemRequest {
        
        private Long productId;
        
        private Integer quantity;
        
        private String sizeName;
    }
}
