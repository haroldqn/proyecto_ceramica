package com.example.backend.repositories;

import com.example.backend.models.Order;
import com.example.backend.models.OrderItem;
import com.example.backend.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la gestión de items de órdenes.
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    
    /**
     * Busca todos los items de una orden.
     */
    List<OrderItem> findByOrderOrderByProduct_NameAsc(Order order);
    
    /**
     * Busca todos los items de una orden por su ID.
     */
    List<OrderItem> findByOrderIdOrderByProduct_NameAsc(Long orderId);
    
    /**
     * Busca items por producto.
     */
    List<OrderItem> findByProduct(Product product);
    
    /**
     * Busca items por producto ID.
     */
    List<OrderItem> findByProductId(Long productId);
    
    /**
     * Cuenta los items de una orden.
     */
    long countByOrder(Order order);
    
    /**
     * Cuenta los items de una orden por su ID.
     */
    long countByOrderId(Long orderId);
    
    /**
     * Calcula la cantidad total vendida de un producto.
     */
    @Query("SELECT COALESCE(SUM(oi.quantity), 0) FROM OrderItem oi WHERE oi.product = :product")
    Integer sumQuantityByProduct(@Param("product") Product product);
    
    /**
     * Calcula la cantidad total vendida de un producto por ID.
     */
    @Query("SELECT COALESCE(SUM(oi.quantity), 0) FROM OrderItem oi WHERE oi.product.id = :productId")
    Integer sumQuantityByProductId(@Param("productId") Long productId);
    
    /**
     * Obtiene los productos más vendidos.
     */
    @Query("SELECT oi.product, SUM(oi.quantity) as totalQuantity FROM OrderItem oi GROUP BY oi.product ORDER BY totalQuantity DESC LIMIT :limit")
    List<Object[]> findTopSellingProducts(@Param("limit") int limit);
    
    /**
     * Obtiene las ventas totales por producto en un período.
     */
    @Query("SELECT oi.product.name, SUM(oi.quantity) FROM OrderItem oi WHERE oi.order.registerDate BETWEEN :startDate AND :endDate GROUP BY oi.product.id, oi.product.name ORDER BY SUM(oi.quantity) DESC")
    List<Object[]> getSalesByProductInPeriod(@Param("startDate") java.time.LocalDateTime startDate, @Param("endDate") java.time.LocalDateTime endDate);
}