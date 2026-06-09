package com.example.backend.repositories;

import com.example.backend.models.Order;
import com.example.backend.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la gestión de pagos.
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    /**
     * Busca pagos por orden.
     */
    List<Payment> findByOrderOrderByPaymentDateDesc(Order order);
    
    /**
     * Busca pagos por orden ID.
     */
    List<Payment> findByOrderIdOrderByPaymentDateDesc(Long orderId);
    
    /**
     * Busca un pago por ID de orden.
     */
    Optional<Payment> findFirstByOrderIdOrderByPaymentDateDesc(Long orderId);
    
    /**
     * Busca pagos por método de pago.
     */
    List<Payment> findByMethodOrderByPaymentDateDesc(String method);
    
    /**
     * Busca pagos en un rango de fechas.
     */
    @Query("SELECT p FROM Payment p WHERE p.paymentDate BETWEEN :startDate AND :endDate ORDER BY p.paymentDate DESC")
    List<Payment> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    /**
     * Cuenta los pagos por método.
     */
    long countByMethod(String method);
    
    /**
     * Suma el total de pagos por método en un período.
     */
    @Query("SELECT COALESCE(SUM(p.order.total), 0) FROM Payment p WHERE p.method = :method AND p.paymentDate BETWEEN :startDate AND :endDate")
    Double sumTotalByMethodAndDateRange(@Param("method") String method, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    /**
     * Obtiene estadísticas de pagos por método.
     */
    @Query("SELECT p.method, COUNT(p) as count, SUM(p.order.total) as total FROM Payment p WHERE p.paymentDate >= :startDate GROUP BY p.method ORDER BY total DESC")
    List<Object[]> getPaymentMethodStats(@Param("startDate") LocalDateTime startDate);
    
    /**
     * Busca pagos por ID de transacción.
     */
    Optional<Payment> findByTransactionId(String transactionId);
}