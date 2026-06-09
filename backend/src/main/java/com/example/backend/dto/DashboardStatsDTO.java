package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para estadísticas del dashboard administrativo.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardStatsDTO {
    
    // Estadísticas de ventas
    private Long totalOrders;
    
    private Long pendingOrders;
    
    private Long processingOrders;
    
    private Long shippedOrders;
    
    private Long deliveredOrders;
    
    private Long cancelledOrders;
    
    // Estadísticas de ingresos
    private BigDecimal totalRevenue;
    
    private BigDecimal monthlyRevenue;
    
    private BigDecimal weeklyRevenue;
    
    // Estadísticas de productos
    private Integer lowStockProducts;
    
    private Integer outOfStockProducts;
    
    // Estadísticas de usuarios
    private Long totalCustomers;
    
    private Long newCustomersThisMonth;
    
    // Métricas de rendimiento
    private Double averageOrderValue;
    
    private Double conversionRate;
}