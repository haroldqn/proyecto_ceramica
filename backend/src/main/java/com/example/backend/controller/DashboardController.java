package com.example.backend.controller;

import com.example.backend.dto.DashboardStatsDTO;
import com.example.backend.models.Order;
import com.example.backend.services.DashboardService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para el dashboard administrativo.
 */
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class DashboardController {
    
    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);
    
    private final DashboardService dashboardService;
    
    /**
     * Obtiene todas las estadísticas del dashboard.
     * GET /api/dashboard/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsDTO> getDashboardStats() {
        logger.info("Solicitud GET /api/dashboard/stats - Obteniendo estadísticas del dashboard");
        
        DashboardStatsDTO stats = dashboardService.getDashboardStats();
        
        return ResponseEntity.ok(stats);
    }
    
    /**
     * Obtiene los productos más vendidos.
     * GET /api/dashboard/top-products
     */
    @GetMapping("/top-products")
    public ResponseEntity<List<Object[]>> getTopSellingProducts(
            @RequestParam(defaultValue = "10") int limit) {
        logger.info("Solicitud GET /api/dashboard/top-products - Obteniendo productos más vendidos");
        
        List<Object[]> products = dashboardService.getTopSellingProducts(limit);
        
        return ResponseEntity.ok(products);
    }
    
    /**
     * Obtiene las órdenes recientes.
     * GET /api/dashboard/recent-orders
     */
    @GetMapping("/recent-orders")
    public ResponseEntity<List<Order>> getRecentOrders(
            @RequestParam(defaultValue = "5") int limit) {
        logger.info("Solicitud GET /api/dashboard/recent-orders - Obteniendo órdenes recientes");
        
        List<Order> orders = dashboardService.getRecentOrders(limit);
        
        return ResponseEntity.ok(orders);
    }
    
    /**
     * Obtiene las ventas por categoría.
     * GET /api/dashboard/sales-by-category
     */
    @GetMapping("/sales-by-category")
    public ResponseEntity<List<Object[]>> getSalesByCategory() {
        logger.info("Solicitud GET /api/dashboard/sales-by-category - Obteniendo ventas por categoría");
        
        List<Object[]> sales = dashboardService.getSalesByCategory();
        
        return ResponseEntity.ok(sales);
    }
}