package com.example.backend.repositories;

import com.example.backend.models.Order;
import com.example.backend.models.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para la gestión de órdenes de compra.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    /**
     * Busca órdenes por cliente.
     */
    List<Order> findByPersonaOrderByRegisterDateDesc(Persona persona);
    
    /**
     * Busca órdenes por cliente.
     */
    List<Order> findByPersonaIdOrderByRegisterDateDesc(Long personaId);
    
    /**
     * Busca órdenes por estado.
     */
    List<Order> findByStatusOrderByRegisterDateDesc(String status);
    
    /**
     * Busca órdenes en un estado específico.
     */
    List<Order> findByStatusIn(List<String> statuses);
    
    /**
     * Cuenta las órdenes por cliente.
     */
    long countByPersonaId(Long personaId);
    
    /**
     * Cuenta las órdenes por estado.
     */
    long countByStatus(String status);
    
    /**
     * Calcula el total vendido en un período.
     */
    @Query("SELECT COALESCE(SUM(o.total), 0) FROM Order o WHERE o.registerDate BETWEEN :startDate AND :endDate")
    Double sumTotalByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    /**
     * Obtiene las órdenes recientes.
     */
    @Query("SELECT o FROM Order o ORDER BY o.registerDate DESC LIMIT :limit")
    List<Order> findRecentOrders(@Param("limit") int limit);
    
    /**
     * Obtiene estadísticas de órdenes por mes.
     */
    @Query("SELECT FUNCTION('MONTH', o.registerDate) as month, FUNCTION('YEAR', o.registerDate) as year, COUNT(o) as count, SUM(o.total) as total " +
           "FROM Order o WHERE o.registerDate >= :startDate GROUP BY month, year ORDER BY year DESC, month DESC")
    List<Object[]> getMonthlyOrderStats(@Param("startDate") LocalDateTime startDate);
}