package com.example.backend.services;

import com.example.backend.dto.OrderRequest;
import com.example.backend.dto.OrderResponse;
import com.example.backend.dto.UpdateOrderStatusRequest;
import com.example.backend.enums.OrderStatus;
import com.example.backend.exceptions.ResourceNotFoundException;
import com.example.backend.models.Order;
import com.example.backend.models.OrderItem;
import com.example.backend.models.Payment;
import com.example.backend.models.Persona;
import com.example.backend.models.Product;
import com.example.backend.repositories.OrderItemRepository;
import com.example.backend.repositories.OrderRepository;
import com.example.backend.repositories.PaymentRepository;
import com.example.backend.repositories.PersonaRepository;
import com.example.backend.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de órdenes de compra.
 */
@Service
@RequiredArgsConstructor
public class OrderService {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PaymentRepository paymentRepository;
    private final PersonaRepository personaRepository;
    private final ProductRepository productRepository;
    
    /**
     * Crea una nueva orden de compra.
     */
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        logger.info("Creando nueva orden para cliente ID: {}", request.getClientId());
        
        // Validar que el cliente exista
        Persona persona = personaRepository.findById(request.getClientId())
                .orElseThrow(() -> {
                    logger.error("Cliente no encontrado: ID {}", request.getClientId());
                    return new ResourceNotFoundException("Cliente no encontrado");
                });
        
        // Crear la orden
        Order order = new Order();
        order.setStatus(OrderStatus.PENDING.getDescription());
        order.setRegisterDate(LocalDateTime.now());
        order.setPersona(persona);
        
        // Calcular el total y crear los items
        double total = 0.0;
        List<OrderItem> orderItems = new ArrayList<>();
        
        for (OrderRequest.OrderItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> {
                        logger.error("Producto no encontrado: ID {}", itemRequest.getProductId());
                        return new ResourceNotFoundException("Producto no encontrado: " + itemRequest.getProductId());
                    });
            
            // Verificar stock
            if (product.getStock() < itemRequest.getQuantity()) {
                logger.error("Stock insuficiente para producto: {}", product.getName());
                throw new IllegalArgumentException("Stock insuficiente para: " + product.getName());
            }
            
            // Obtener precio según tamaño si aplica
            BigDecimal unitPrice = product.getPrice();
            if (itemRequest.getSizeName() != null && product.getSizes() != null) {
                for (var size : product.getSizes()) {
                    if (size.getName().equalsIgnoreCase(itemRequest.getSizeName())) {
                        unitPrice = productRepository.findPriceForSize(product.getId(), size.getId())
                                .orElse(product.getPrice());
                        break;
                    }
                }
            }
            
            double subtotal = unitPrice.multiply(BigDecimal.valueOf(itemRequest.getQuantity()))
                    .setScale(2, RoundingMode.HALF_UP).doubleValue();
            total += subtotal;
            
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setUnitPrice(unitPrice.doubleValue());
            orderItems.add(orderItem);
            
            // Actualizar stock
            product.setStock(product.getStock() - itemRequest.getQuantity());
            productRepository.save(product);
            
            logger.info("Agregado item: {} x{} - S/{}", product.getName(), itemRequest.getQuantity(), subtotal);
        }
        
        order.setTotal(total);
        Order savedOrder = orderRepository.save(order);
        
        // Guardar items
        orderItemRepository.saveAll(orderItems);
        
        logger.info("Orden creada exitosamente: ID {}", savedOrder.getId());
        
        return convertToResponse(savedOrder);
    }
    
    /**
     * Obtiene una orden por su ID.
     */
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {
        logger.info("Buscando orden ID: {}", id);
        
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Orden no encontrada: ID {}", id);
                    return new ResourceNotFoundException("Orden no encontrada");
                });
        
        return convertToResponse(order);
    }
    
    /**
     * Obtiene todas las órdenes de un cliente.
     */
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByClient(Long clientId) {
        logger.info("Buscando órdenes del cliente ID: {}", clientId);
        
        List<Order> orders = orderRepository.findByPersonaIdOrderByRegisterDateDesc(clientId);
        
        return orders.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtiene todas las órdenes.
     */
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        logger.info("Obteniendo todas las órdenes");
        
        List<Order> orders = orderRepository.findAll();
        
        return orders.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Actualiza el estado de una orden.
     */
    @Transactional
    public OrderResponse updateOrderStatus(Long id, UpdateOrderStatusRequest request) {
        logger.info("Actualizando estado de orden ID: {}", id);
        
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Orden no encontrada: ID {}", id);
                    return new ResourceNotFoundException("Orden no encontrada");
                });
        
        order.setStatus(request.getStatus());
        
        if (request.getTrackingNumber() != null) {
            // Aquí se podría agregar un campo trackingNumber a la entidad Order
            logger.info("Número de seguimiento: {}", request.getTrackingNumber());
        }
        
        Order updatedOrder = orderRepository.save(order);
        
        logger.info("Estado de orden actualizado: ID {} - {}", id, request.getStatus());
        
        return convertToResponse(updatedOrder);
    }
    
    /**
     * Cancela una orden.
     */
    @Transactional
    public void cancelOrder(Long id) {
        logger.info("Cancelando orden ID: {}", id);
        
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Orden no encontrada: ID {}", id);
                    return new ResourceNotFoundException("Orden no encontrada");
                });
        
        // Verificar si se puede cancelar
        if (order.getStatus().equals(OrderStatus.SHIPPED.getDescription()) ||
            order.getStatus().equals(OrderStatus.DELIVERED.getDescription())) {
            throw new IllegalStateException("No se puede cancelar una orden enviada o entregada");
        }
        
        // Restaurar stock
        List<OrderItem> items = orderItemRepository.findByOrderIdOrderByProduct_NameAsc(id);
        for (OrderItem item : items) {
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
            productRepository.save(product);
        }
        
        order.setStatus(OrderStatus.CANCELLED.getDescription());
        orderRepository.save(order);
        
        logger.info("Orden cancelada exitosamente: ID {}", id);
    }
    
    /**
     * Obtiene estadísticas de órdenes.
     */
    @Transactional(readOnly = true)
    public java.util.Map<String, Long> getOrderStats() {
        logger.info("Obteniendo estadísticas de órdenes");
        
        long totalOrders = orderRepository.count();
        long pendingOrders = orderRepository.countByStatus(OrderStatus.PENDING.getDescription());
        long processingOrders = orderRepository.countByStatus(OrderStatus.PROCESSING.getDescription());
        long shippedOrders = orderRepository.countByStatus(OrderStatus.SHIPPED.getDescription());
        long deliveredOrders = orderRepository.countByStatus(OrderStatus.DELIVERED.getDescription());
        long cancelledOrders = orderRepository.countByStatus(OrderStatus.CANCELLED.getDescription());
        
        return java.util.Map.of(
            "totalOrders", totalOrders,
            "pendingOrders", pendingOrders,
            "processingOrders", processingOrders,
            "shippedOrders", shippedOrders,
            "deliveredOrders", deliveredOrders,
            "cancelledOrders", cancelledOrders
        );
    }
    
    /**
     * Convierte una entidad Order a OrderResponse.
     */
    private OrderResponse convertToResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(Long.valueOf(order.getId()));
        response.setOrderNumber("ORD-" + String.format("%06d", order.getId()));
        response.setStatus(order.getStatus());
        response.setRegisterDate(order.getRegisterDate());
        response.setTotal(order.getTotal());
        
        if (order.getPersona() != null) {
            OrderResponse.ClientInfo clientInfo = new OrderResponse.ClientInfo();
            clientInfo.setId(Long.valueOf(order.getPersona().getId()));
            clientInfo.setName(order.getPersona().getName());
            response.setClient(clientInfo);
        }
        
        // Obtener items
        List<OrderItem> items = orderItemRepository.findByOrderIdOrderByProduct_NameAsc(Long.valueOf(order.getId()));
        List<OrderResponse.OrderItemResponse> itemResponses = items.stream()
                .map(item -> {
                    OrderResponse.OrderItemResponse itemResponse = new OrderResponse.OrderItemResponse();
                    itemResponse.setId(item.getId());
                    itemResponse.setProductId(item.getProduct().getId());
                    itemResponse.setProductName(item.getProduct().getName());
                    itemResponse.setProductImageUrl(item.getProduct().getImageUrl());
                    itemResponse.setQuantity(item.getQuantity());
                    itemResponse.setUnitPrice(item.getUnitPrice());
                    itemResponse.setSubtotal(item.getUnitPrice() * item.getQuantity());
                    return itemResponse;
                })
                .collect(Collectors.toList());
        response.setItems(itemResponses);
        
        return response;
    }
}