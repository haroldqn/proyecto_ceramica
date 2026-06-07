package com.example.backend.repositories;

import com.example.backend.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    @Query(value = "SELECT * FROM products WHERE id_category = :categoryId AND id_product != :productId LIMIT 5", nativeQuery = true)
    List<Product> recommendByCategory(@Param("categoryId") Long categoryId, @Param("productId") Long productId);

    List<Product> findTop4ByCategoryIdAndStatusTrue(Long categoryId);

    List<Product> findByStatusTrue();
    
    /**
     * Obtiene un producto destacado por categoría (el primero de cada categoría)
     * Retorna 5 productos, uno por cada categoría
     */
    @Query(value = "SELECT * FROM products p WHERE p.status = 1 AND p.stock > 0 ORDER BY p.id_category, p.id_product LIMIT 5", nativeQuery = true)
    List<Product> findFeaturedHomeProducts();
    
    /**
     * Obtiene los primeros productos activos ordenados por categoría
     */
    @Query(value = "SELECT * FROM products WHERE status = 1 AND stock > 0 ORDER BY id_category, id_product", nativeQuery = true)
    List<Product> findActiveProductsOrderedByCategory();

    /**
     * Obtiene el precio específico para un producto y tamaño desde la tabla product_size_pricing
     */
    @Query(value = "SELECT price FROM product_size_pricing WHERE id_product = :productId AND id_size = :sizeId", nativeQuery = true)
    Optional<BigDecimal> findPriceForSize(@Param("productId") Long productId, @Param("sizeId") Long sizeId);
}
