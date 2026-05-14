package com.example.backend.services;

import com.example.backend.dto.*;
import com.example.backend.models.Category;
import com.example.backend.models.Product;
import com.example.backend.repositories.CategoryRepository;
import com.example.backend.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public ProductDetailResponse getProductDetailsById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Este producto no Existe"));

        boolean status = product.getStock() >= 0; // faltaba el "=" :v

        List<RelatedProductDTO> relatedProductDTO = productRepository
                .recommendByCategory(product.getCategory().getId(), product.getId())
                .stream()
                .map(RelatedProductDTO::new)
                .collect(Collectors.toList());

        // coleccion de talla
        List<SizeDTO> sizesDTO = product.getSizes().
                stream()
                .map(size -> new SizeDTO(
                        size.getId(),
                        size.getName(),
                        size.getDimension()
                ))
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

    // Logica para el Admin
    // Obtener Lista de Productos
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

    // Crear Producto
    public AdminProductResponse createProduct(ProductRequest request) {

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));

        Product product = new Product();

        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(category);

        // si no mandan status
        product.setStatus(
                request.getStatus() != null
                        ? request.getStatus()
                        : request.getStock() > 0
        );

        Product saved = productRepository.save(product);

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

    // Actualizar un Producto
    public AdminProductResponse updateProduct(Long id, ProductRequest request) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

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

    // Borrar un Producto
    public void deleteProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        product.setStatus(false);

        productRepository.save(product);
    }
}
