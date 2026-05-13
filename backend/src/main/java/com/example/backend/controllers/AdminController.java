package com.example.backend.controllers;

import com.example.backend.dto.AdminProductResponse;
import com.example.backend.dto.AdminUserResponse;
import com.example.backend.models.Product;
import com.example.backend.repositories.ProductRepository;
import com.example.backend.repositories.UserRepository;
import com.example.backend.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final ProductService productService;
    private final UserRepository userRepository;

    @GetMapping("/test")
    public String test() {
        return "Admin funcionando";
    }

    @GetMapping("/users")
    public List<AdminUserResponse> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new AdminUserResponse(
                user.getId(),
                user.getPersona().getName(),
                user.getEmail(),
                user.getRole().getName()
        )).toList();
    }

    // Rutas de Producto
    @GetMapping("/products")
    public List<AdminProductResponse> getProducts() {
        return productService.getAllAdminProducts();
    }

    @PostMapping("/products")
    public Product createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    @PutMapping("/products/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return productService.updateProduct(id, product);
    }

    @DeleteMapping("/products/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
