package com.example.backend.controller;

import com.example.backend.dto.PaymentRequest;
import com.example.backend.dto.PaymentResponse;
import com.example.backend.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la gestión de pagos.
 */
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    
    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
    
    private final PaymentService paymentService;
    
    /**
     * Procesa un pago para una orden.
     * POST /api/payments/process
     */
    @PostMapping("/process")
    public ResponseEntity<PaymentResponse> processPayment(@RequestBody PaymentRequest request) {
        logger.info("Solicitud POST /api/payments/process - Procesando pago");
        
        PaymentResponse response = paymentService.processPayment(request);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Obtiene un pago por su ID.
     * GET /api/payments/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable Long id) {
        logger.info("Solicitud GET /api/payments/{} - Obteniendo pago", id);
        
        PaymentResponse response = paymentService.getPaymentById(id);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Obtiene los pagos de una orden.
     * GET /api/payments/order/{orderId}
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByOrder(@PathVariable Long orderId) {
        logger.info("Solicitud GET /api/payments/order/{} - Obteniendo pagos de la orden", orderId);
        
        List<PaymentResponse> responses = paymentService.getPaymentsByOrder(orderId);
        
        return ResponseEntity.ok(responses);
    }
    
    /**
     * Obtiene todos los pagos (solo administradores).
     * GET /api/payments
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        logger.info("Solicitud GET /api/payments - Obteniendo todos los pagos");
        
        List<PaymentResponse> responses = paymentService.getAllPayments();
        
        return ResponseEntity.ok(responses);
    }
    
    /**
     * Obtiene estadísticas de métodos de pago (solo administradores).
     * GET /api/payments/stats/methods
     */
    @GetMapping("/stats/methods")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> getPaymentMethodStats() {
        logger.info("Solicitud GET /api/payments/stats/methods - Obteniendo estadísticas de métodos");
        
        Object stats = paymentService.getPaymentMethodStats();
        
        return ResponseEntity.ok(stats);
    }
    
    /**
     * Obtiene el total recaudado en un período (solo administradores).
     * GET /api/payments/stats/total
     */
    @GetMapping("/stats/total")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getTotalByDateRange(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        logger.info("Solicitud GET /api/payments/stats/total - Obteniendo total del período");
        
        BigDecimal total = paymentService.getTotalByDateRange(startDate, endDate);
        
        return ResponseEntity.ok(Map.of(
            "startDate", startDate,
            "endDate", endDate,
            "total", total
        ));
    }
}