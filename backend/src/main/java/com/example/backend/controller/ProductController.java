package com.example.backend.controller;

import com.example.backend.models.Product;
import com.example.backend.repositories.ProductRepository;
import com.example.backend.dto.ProductDetailResponse;
import com.example.backend.dto.HomeProductDTO;
import com.example.backend.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailResponse> getProduct(@PathVariable Long id) {
        ProductDetailResponse response = productService.getProductDetailsById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para obtener productos destacados del home
     * Retorna 5 productos, uno por cada categoría
     * GET /api/products/home
     */
    @GetMapping("/home")
    public ResponseEntity<List<HomeProductDTO>> getFeaturedHomeProducts() {
        List<HomeProductDTO> response = productService.getFeaturedHomeProducts();
        return ResponseEntity.ok(response);
    }
}
