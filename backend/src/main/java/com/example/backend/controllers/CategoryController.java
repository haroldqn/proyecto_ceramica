package com.example.backend.controllers;

import com.example.backend.models.Category;
import com.example.backend.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.backend.dto.CategoryDTO;
import com.example.backend.dto.CategoryShowcaseDTO;
import com.example.backend.services.CategoryService;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @GetMapping("/all")
    public ResponseEntity<List<CategoryShowcaseDTO>> getCategoriesForHome() {
        List<CategoryShowcaseDTO> response = categoryService.getHomeCategoriesWithProducts();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list")
    public ResponseEntity<List<CategoryDTO>> getCategories() {
        List<CategoryDTO> response = categoryService.getHomeCategories();
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para obtener categorías del home
     * Retorna: nombre, imagen, descripción corta
     * GET /api/categories/home
     */
    @GetMapping("/home")
    public ResponseEntity<List<CategoryDTO>> getHomeCategories() {
        List<CategoryDTO> response = categoryService.getHomeCategories();
        return ResponseEntity.ok(response);
    }
}
