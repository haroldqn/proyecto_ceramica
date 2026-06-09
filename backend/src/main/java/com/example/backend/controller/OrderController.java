package com.example.backend.controller;

import com.example.backend.dto.OrderRequest;
import com.example.backend.dto.OrderResponse;
import com.example.backend.dto.UpdateOrderStatusRequest;
import com.example.backend.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la gestión de órdenes de compra.
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    
    private final OrderService orderService;
    
    /**
     * Crea una nueva orden de compra.
     * POST /api/orders
     */
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) {
        logger.info("Solicitud POST /api/orders - Creando orden");
        
        OrderResponse response = orderService.createOrder(request);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Obtiene una orden por su ID.
     * GET /api/orders/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        logger.info("Solicitud GET /api/orders/{} - Obteniendo orden", id);
        
        OrderResponse response = orderService.getOrderById(id);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Obtiene todas las órdenes de un cliente.
     * GET /api/orders/client/{clientId}
     */
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByClient(@PathVariable Long clientId) {
        logger.info("Solicitud GET /api/orders/client/{} - Obteniendo órdenes del cliente", clientId);
        
        List<OrderResponse> responses = orderService.getOrdersByClient(clientId);
        
        return ResponseEntity.ok(responses);
    }
    
    /**
     * Obtiene todas las órdenes (solo administradores).
     * GET /api/orders
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        logger.info("Solicitud GET /api/orders - Obteniendo todas las órdenes");
        
        List<OrderResponse> responses = orderService.getAllOrders();
        
        return ResponseEntity.ok(responses);
    }
    
    /**
     * Actualiza el estado de una orden (solo administradores).
     * PUT /api/orders/{id}/status
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long id,
            @RequestBody UpdateOrderStatusRequest request) {
        logger.info("Solicitud PUT /api/orders/{}/status - Actualizando estado", id);
        
        OrderResponse response = orderService.updateOrderStatus(id, request);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Cancela una orden.
     * DELETE /api/orders/{id}/cancel
     */
    @DeleteMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
        logger.info("Solicitud DELETE /api/orders/{}/cancel - Cancelando orden", id);
        
        orderService.cancelOrder(id);
        
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Obtiene estadísticas de órdenes (solo administradores).
     * GET /api/orders/stats
     */
    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getOrderStats() {
        logger.info("Solicitud GET /api/orders/stats - Obteniendo estadísticas");
        
        Map<String, Long> stats = orderService.getOrderStats();
        
        Map<String, Object> response = new HashMap<>(stats);
        
        return ResponseEntity.ok(response);
    }
}