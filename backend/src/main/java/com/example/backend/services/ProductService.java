package com.example.backend.services;

import com.example.backend.dto.*;
import com.example.backend.models.Category;
import com.example.backend.models.Product;
import com.example.backend.repositories.CategoryRepository;
import com.example.backend.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public ProductDetailResponse getProductDetailsById(Long id) {
        logger.info("Buscando producto id {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Producto no encontrado. Id: {}", id);
                    return new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "este producto no Existe"
                    );
                });

        logger.info("Producto encontrado: {}", product.getName());

        boolean status = product.getStock() > 0;

        List<RelatedProductDTO> relatedProductDTO = productRepository
                .recommendByCategory(product.getCategory().getId(), product.getId())
                .stream()
                .map(RelatedProductDTO::new)
                .collect(Collectors.toList());

        // Obtener precios por tamaño desde la tabla product_size_pricing
        List<SizeDTO> sizesDTO = product.getSizes().stream()
                .map(size -> {
                    // Buscar el precio específico para este producto y tamaño
                    BigDecimal sizePrice = productRepository.findPriceForSize(product.getId(), size.getId())
                            .orElse(product.getPrice()); // Fallback al precio del producto
                    return new SizeDTO(
                            size.getId(),
                            size.getName(),
                            size.getDimension(),
                            sizePrice
                    );
                })
                .collect(Collectors.toList());

        return new ProductDetailResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStock(),
                product.getImageUrl(),
                status,
                product.getCategory().getName(),
                product.getCategory().getDescription(),
                sizesDTO,
                relatedProductDTO
        );
    }

    public List<AdminProductResponse> getAllAdminProducts() {
        return productRepository.findByStatusTrue()
                .stream()
                .map(product -> new AdminProductResponse(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getStock(),
                        product.getImageUrl(),
                        product.isStatus(),
                        product.getCategory().getId(),
                        product.getCategory().getName()
                ))
                .toList();
    }

    public AdminProductResponse createProduct(ProductRequest request) {
        logger.info("Creando producto...");

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> {
                    logger.error("Categoria no encontrada: id {}", request.getCategoryId());
                    return new RuntimeException("Categoria no encontrada");
                });

        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(category);

        product.setStatus(
                request.getStatus() != null
                        ? request.getStatus()
                        : request.getStock() > 0
        );

        Product saved = productRepository.save(product);
        logger.info("Producto creado con id: {}", saved.getId());

        return new AdminProductResponse(
                saved.getId(),
                saved.getName(),
                saved.getPrice(),
                saved.getStock(),
                saved.getImageUrl(),
                saved.isStatus(),
                saved.getCategory().getId(),
                saved.getCategory().getName()
        );
    }

    public AdminProductResponse updateProduct(Long id, ProductRequest request) {
        logger.info("Buscando producto con id {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Producto no encontrado. Id: {}", id);
                    return new RuntimeException("Producto no encontrado");
                });

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));

        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(category);

        if (request.getStatus() != null) {
            product.setStatus(request.getStatus());
        } else {
            product.setStatus(request.getStock() > 0);
        }

        Product updatedProduct = productRepository.save(product);
        logger.info("Producto actualizado con id: {}", updatedProduct.getId());

        return new AdminProductResponse(
                updatedProduct.getId(),
                updatedProduct.getName(),
                updatedProduct.getPrice(),
                updatedProduct.getStock(),
                updatedProduct.getImageUrl(),
                updatedProduct.isStatus(),
                updatedProduct.getCategory().getId(),
                updatedProduct.getCategory().getName()
        );
    }

    public void deleteProduct(Long id) {
        logger.info("Buscando producto con id {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Producto no encontrado. Id: {}", id);
                    return new RuntimeException("Producto no encontrado");
                });

        product.setStatus(false);
        productRepository.save(product);
        logger.info("Producto inactivo: {}", id);
    }

    /**
     * Obtiene todos los productos activos para el home
     * Retorna todos los productos con status activo
     */
    public List<HomeProductDTO> getFeaturedHomeProducts() {
        logger.info("Obteniendo todos los productos activos para home");
        
        List<Product> allProducts = productRepository.findByStatusTrue();
        
        return allProducts.stream().map(product -> {
            String shortDesc = product.getShortDescription() != null 
                    ? product.getShortDescription() 
                    : "Hermosa pieza de ceramica artesanal de la coleccion El Mundo de Mery";
            String categoryLabel = product.getCategory() != null ? product.getCategory().getLabel() : "";
            
            return new HomeProductDTO(
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    product.getImageUrl(),
                    shortDesc,
                    categoryLabel
            );
        }).collect(Collectors.toList());
    }
}
