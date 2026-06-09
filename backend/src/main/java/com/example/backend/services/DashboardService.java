package com.example.backend.services;

import com.example.backend.dto.DashboardStatsDTO;
import com.example.backend.enums.OrderStatus;
import com.example.backend.repositories.OrderItemRepository;
import com.example.backend.repositories.OrderRepository;
import com.example.backend.repositories.PaymentRepository;
import com.example.backend.repositories.ProductRepository;
import com.example.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio para estadísticas del dashboard administrativo.
 */
@Service
@RequiredArgsConstructor
public class DashboardService {
    
    private static final Logger logger = LoggerFactory.getLogger(DashboardService.class);
    
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PaymentRepository paymentRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    
    /**
     * Obtiene todas las estadísticas para el dashboard.
     */
    @Transactional(readOnly = true)
    public DashboardStatsDTO getDashboardStats() {
        logger.info("Obteniendo estadísticas del dashboard");
        
        DashboardStatsDTO stats = new DashboardStatsDTO();
        
        // Estadísticas de órdenes
        long totalOrders = orderRepository.count();
        long pendingOrders = orderRepository.countByStatus(OrderStatus.PENDING.getDescription());
        long processingOrders = orderRepository.countByStatus(OrderStatus.PROCESSING.getDescription());
        long shippedOrders = orderRepository.countByStatus(OrderStatus.SHIPPED.getDescription());
        long deliveredOrders = orderRepository.countByStatus(OrderStatus.DELIVERED.getDescription());
        long cancelledOrders = orderRepository.countByStatus(OrderStatus.CANCELLED.getDescription());
        
        stats.setTotalOrders(totalOrders);
        stats.setPendingOrders(pendingOrders);
        stats.setProcessingOrders(processingOrders);
        stats.setShippedOrders(shippedOrders);
        stats.setDeliveredOrders(deliveredOrders);
        stats.setCancelledOrders(cancelledOrders);
        
        // Estadísticas de ingresos
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime startOfWeek = now.minusWeeks(1);
        
        Double monthlyRevenue = paymentRepository.sumTotalByMethodAndDateRange("", startOfMonth, now);
        Double weeklyRevenue = paymentRepository.sumTotalByMethodAndDateRange("", startOfWeek, now);
        
        stats.setTotalRevenue(BigDecimal.valueOf(monthlyRevenue != null ? monthlyRevenue : 0));
        stats.setMonthlyRevenue(BigDecimal.valueOf(monthlyRevenue != null ? monthlyRevenue : 0));
        stats.setWeeklyRevenue(BigDecimal.valueOf(weeklyRevenue != null ? weeklyRevenue : 0));
        
        // Estadísticas de productos
        long totalProducts = productRepository.count();
        long lowStockProducts = productRepository.count(); // Se debería filtrar por stock bajo
        long outOfStockProducts = productRepository.count(); // Se debería filtrar por stock agotado
        
        stats.setLowStockProducts((int) lowStockProducts);
        stats.setOutOfStockProducts((int) outOfStockProducts);
        
        // Estadísticas de usuarios
        long totalCustomers = userRepository.count();
        long newCustomersThisMonth = userRepository.count(); // Se debería filtrar por fecha
        
        stats.setTotalCustomers(totalCustomers);
        stats.setNewCustomersThisMonth(newCustomersThisMonth);
        
        // Métricas de rendimiento
        if (totalOrders > 0) {
            stats.setAverageOrderValue(monthlyRevenue != null ? monthlyRevenue / totalOrders : 0.0);
        } else {
            stats.setAverageOrderValue(0.0);
        }
        
        stats.setConversionRate(2.5); // Valor placeholder
        
        return stats;
    }
    
    /**
     * Obtiene las ventas por categoría.
     */
    @Transactional(readOnly = true)
    public List<Object[]> getSalesByCategory() {
        logger.info("Obteniendo ventas por categoría");
        // Implementación pendiente
        return List.of();
    }
    
    /**
     * Obtiene los productos más vendidos.
     */
    @Transactional(readOnly = true)
    public List<Object[]> getTopSellingProducts(int limit) {
        logger.info("Obteniendo productos más vendidos, límite: {}", limit);
        return orderItemRepository.findTopSellingProducts(limit);
    }
    
    /**
     * Obtiene las órdenes recientes.
     */
    @Transactional(readOnly = true)
    public List<com.example.backend.models.Order> getRecentOrders(int limit) {
        logger.info("Obteniendo órdenes recientes, límite: {}", limit);
        return orderRepository.findRecentOrders(limit);
    }
}