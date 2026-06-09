package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para la respuesta de una orden de compra.
 * Contiene toda la información de la orden procesada.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    
    private Long id;
    
    private String orderNumber;
    
    private String status;
    
    private LocalDateTime registerDate;
    
    private Double total;
    
    private String paymentMethod;
    
    private String paymentStatus;
    
    private ClientInfo client;
    
    private List<OrderItemResponse> items;
    
    private String shippingAddress;
    
    private String trackingNumber;
    
    /**
     * Información básica del cliente
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ClientInfo {
        
        private Long id;
        
        private String name;
        
        private String email;
        
        private String phone;
    }
    
    /**
     * DTO para los items de una orden en la respuesta
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderItemResponse {
        
        private Long id;
        
        private Long productId;
        
        private String productName;
        
        private String productImageUrl;
        
        private Integer quantity;
        
        private Double unitPrice;
        
        private Double subtotal;
        
        private String sizeName;
    }
}