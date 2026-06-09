package com.example.backend.services;

import com.example.backend.dto.PaymentRequest;
import com.example.backend.dto.PaymentResponse;
import com.example.backend.enums.OrderStatus;
import com.example.backend.exceptions.ResourceNotFoundException;
import com.example.backend.models.Order;
import com.example.backend.models.Payment;
import com.example.backend.repositories.OrderRepository;
import com.example.backend.repositories.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de pagos.
 */
@Service
@RequiredArgsConstructor
public class PaymentService {
    
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
    
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    
    /**
     * Procesa un pago para una orden.
     */
    @Transactional
    public PaymentResponse processPayment(PaymentRequest request) {
        logger.info("Procesando pago para orden ID: {}", request.getOrderId());
        
        // Validar que la orden exista
        Order order = orderRepository.findById(Long.valueOf(request.getOrderId()))
                .orElseThrow(() -> {
                    logger.error("Orden no encontrada: ID {}", request.getOrderId());
                    return new ResourceNotFoundException("Orden no encontrada");
                });
        
        // Verificar que la orden esté pendiente
        if (!order.getStatus().equals(OrderStatus.PENDING.getDescription())) {
            throw new IllegalStateException("La orden no está en estado pendiente");
        }
        
        // Crear el pago
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setMethod(request.getPaymentMethod());
        
        Payment savedPayment = paymentRepository.save(payment);
        
        // Actualizar estado de la orden a confirmado
        order.setStatus(OrderStatus.CONFIRMED.getDescription());
        orderRepository.save(order);
        
        String transactionId = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        logger.info("Pago procesado exitosamente: Transacción {}", transactionId);
        
        return convertToResponse(savedPayment, transactionId);
    }
    
    /**
     * Obtiene un pago por su ID.
     */
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentById(Long id) {
        logger.info("Buscando pago ID: {}", id);
        
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Pago no encontrado: ID {}", id);
                    return new ResourceNotFoundException("Pago no encontrado");
                });
        
        return convertToResponse(payment);
    }
    
    /**
     * Obtiene los pagos de una orden.
     */
    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByOrder(Long orderId) {
        logger.info("Buscando pagos de la orden ID: {}", orderId);
        
        List<Payment> payments = paymentRepository.findByOrderIdOrderByPaymentDateDesc(orderId);
        
        return payments.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtiene todos los pagos.
     */
    @Transactional(readOnly = true)
    public List<PaymentResponse> getAllPayments() {
        logger.info("Obteniendo todos los pagos");
        
        List<Payment> payments = paymentRepository.findAll();
        
        return payments.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtiene estadísticas de pagos por método.
     */
    @Transactional(readOnly = true)
    public Object getPaymentMethodStats() {
        logger.info("Obteniendo estadísticas de métodos de pago");
        
        List<Object[]> stats = paymentRepository.getPaymentMethodStats(
            LocalDateTime.now().minusMonths(1)
        );
        
        return stats.stream()
                .map(stat -> {
                    return java.util.Map.of(
                        "method", stat[0],
                        "count", ((Number) stat[1]).longValue(),
                        "total", ((Number) stat[2]).doubleValue()
                    );
                })
                .collect(Collectors.toList());
    }
    
    /**
     * Obtiene el total recaudado en un período.
     */
    @Transactional(readOnly = true)
    public BigDecimal getTotalByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        logger.info("Calculando total recaudado entre {} y {}", startDate, endDate);
        
        Double total = paymentRepository.sumTotalByMethodAndDateRange("", startDate, endDate);
        
        return BigDecimal.valueOf(total != null ? total : 0);
    }
    
    /**
     * Convierte una entidad Payment a PaymentResponse.
     */
    private PaymentResponse convertToResponse(Payment payment) {
        return convertToResponse(payment, "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
    }
    
    /**
     * Convierte una entidad Payment a PaymentResponse con transactionId específico.
     */
    private PaymentResponse convertToResponse(Payment payment, String transactionId) {
        PaymentResponse response = new PaymentResponse();
        response.setId(payment.getId());
        response.setOrderId(Long.valueOf(payment.getOrder().getId()));
        response.setOrderNumber("ORD-" + String.format("%06d", payment.getOrder().getId()));
        response.setPaymentMethod(payment.getMethod());
        response.setStatus("COMPLETED");
        response.setAmount(BigDecimal.valueOf(payment.getOrder().getTotal()));
        response.setPaymentDate(payment.getPaymentDate());
        response.setTransactionId(transactionId);
        response.setCreatedAt(payment.getPaymentDate());
        
        return response;
    }
}