package com.example.backend.services;

import com.example.backend.dto.AdminProductResponse;
import com.example.backend.dto.ProductDetailResponse;
import com.example.backend.dto.RelatedProductDTO;
import com.example.backend.dto.SizeDTO;
import com.example.backend.models.Product;
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
        return productRepository.findAll()
                .stream()
                .map(product -> new AdminProductResponse(
                        product.getId(),
                        product.getName(),
                        product.getPrice().doubleValue(),
                        product.getStock(),
                        product.getCategory().getName()
                ))
                .toList();
    }

    // Crear Producto
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    // Actualizar un Producto
    public Product updateProduct(Long id, Product updated) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (updated.getName()!=null) product.setName(updated.getName());
        if (updated.getPrice()!=null) product.setPrice(updated.getPrice());
        if (updated.getStock()!=null) product.setStock(updated.getStock());
        if (updated.getImageUrl()!=null) product.setImageUrl(updated.getImageUrl());
        if (updated.getCategory()!=null) product.setCategory(updated.getCategory());

        product.setStatus(updated.isStatus());

        return productRepository.save(product);
    }

    // Borrar un Producto
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        productRepository.delete(product);
    }
}
