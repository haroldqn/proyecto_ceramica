package com.example.backend.services;

import com.example.backend.dto.AdminProductResponse;
import com.example.backend.dto.HomeProductDTO;
import com.example.backend.dto.ProductDetailResponse;
import com.example.backend.dto.ProductRequest;
import com.example.backend.models.Category;
import com.example.backend.models.Product;
import com.example.backend.models.Size;
import com.example.backend.repositories.CategoryRepository;
import com.example.backend.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;
    private Category testCategory;
    private Size testSize;

    @BeforeEach
    void setUp() {
        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Dijes");
        testCategory.setDescription("Pequeñas piezas de cerámica");
        testCategory.setLabel("DIJES");
        testCategory.setImageUrl("/categorias/Dijes/test.png");
        testCategory.setPrecioBase(BigDecimal.valueOf(0.70));

        testSize = new Size();
        testSize.setId(1L);
        testSize.setName("Pequeño");
        testSize.setDimension("2x3 cm");

        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Aguila Calva");
        testProduct.setPrice(BigDecimal.valueOf(0.70));
        testProduct.setStock(10);
        testProduct.setImageUrl("/productos/aguila.png");
        testProduct.setShortDescription("Hermosa pieza de cerámica");
        testProduct.setStatus(true);
        testProduct.setCategory(testCategory);
        testProduct.setSizes(List.of(testSize));
    }

    @Test
    void getProductDetailsById_WhenProductExists_ReturnsProductDetailResponse() {
        // Given
        Long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.of(testProduct));
        when(productRepository.findPriceForSize(productId, testSize.getId()))
                .thenReturn(Optional.of(BigDecimal.valueOf(0.80)));

        // When
        ProductDetailResponse response = productService.getProductDetailsById(productId);

        // Then
        assertNotNull(response);
        assertEquals(testProduct.getId(), response.getId());
        assertEquals(testProduct.getName(), response.getName());
        assertEquals(testProduct.getPrice(), response.getPrice());
        assertEquals(testProduct.getStock(), response.getStock());
        assertTrue(response.isStatus());
        assertEquals(testCategory.getName(), response.getCategoryName());
        assertFalse(response.getSizes().isEmpty());
        assertEquals(BigDecimal.valueOf(0.80), response.getSizes().get(0).getPrice());
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void getProductDetailsById_WhenProductNotExists_ThrowsResponseStatusException() {
        // Given
        Long productId = 999L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // When & Then
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> productService.getProductDetailsById(productId)
        );
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void getAllAdminProducts_ReturnsAllActiveProducts() {
        // Given
        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Buho Coral");
        product2.setPrice(BigDecimal.valueOf(0.70));
        product2.setStock(5);
        product2.setImageUrl("/productos/buho.png");
        product2.setStatus(true);
        product2.setCategory(testCategory);

        when(productRepository.findByStatusTrue()).thenReturn(List.of(testProduct, product2));

        // When
        List<AdminProductResponse> result = productService.getAllAdminProducts();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testProduct.getId(), result.get(0).getId());
        assertEquals(product2.getId(), result.get(1).getId());
        verify(productRepository, times(1)).findByStatusTrue();
    }

    @Test
    void getAllAdminProducts_WhenNoProducts_ReturnsEmptyList() {
        // Given
        when(productRepository.findByStatusTrue()).thenReturn(List.of());

        // When
        List<AdminProductResponse> result = productService.getAllAdminProducts();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productRepository, times(1)).findByStatusTrue();
    }

    @Test
    void createProduct_WithValidRequest_CreatesAndReturnsProduct() {
        // Given
        ProductRequest request = new ProductRequest();
        request.setName("Nuevo Producto");
        request.setPrice(BigDecimal.valueOf(1.50));
        request.setStock(20);
        request.setImageUrl("/productos/nuevo.png");
        request.setCategoryId(1L);
        request.setStatus(null);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // When
        AdminProductResponse response = productService.createProduct(request);

        // Then
        assertNotNull(response);
        assertEquals(testProduct.getId(), response.getId());
        verify(categoryRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void createProduct_WithInvalidCategory_ThrowsRuntimeException() {
        // Given
        ProductRequest request = new ProductRequest();
        request.setName("Nuevo Producto");
        request.setPrice(BigDecimal.valueOf(1.50));
        request.setStock(20);
        request.setImageUrl("/productos/nuevo.png");
        request.setCategoryId(999L);
        request.setStatus(null);

        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> productService.createProduct(request)
        );
        assertEquals("Categoria no encontrada", exception.getMessage());
        verify(categoryRepository, times(1)).findById(999L);
        verify(productRepository, never()).save(any());
    }

    @Test
    void updateProduct_WithValidRequest_UpdatesAndReturnsProduct() {
        // Given
        Long productId = 1L;
        ProductRequest request = new ProductRequest();
        request.setName("Producto Actualizado");
        request.setPrice(BigDecimal.valueOf(2.00));
        request.setStock(15);
        request.setImageUrl("/productos/actualizado.png");
        request.setCategoryId(1L);
        request.setStatus(true);

        when(productRepository.findById(productId)).thenReturn(Optional.of(testProduct));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // When
        AdminProductResponse response = productService.updateProduct(productId, request);

        // Then
        assertNotNull(response);
        verify(productRepository, times(1)).findById(productId);
        verify(categoryRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(testProduct);
    }

    @Test
    void updateProduct_WhenProductNotExists_ThrowsRuntimeException() {
        // Given
        Long productId = 999L;
        ProductRequest request = new ProductRequest();
        request.setName("Producto Actualizado");
        request.setPrice(BigDecimal.valueOf(2.00));
        request.setStock(15);
        request.setImageUrl("/productos/actualizado.png");
        request.setCategoryId(1L);
        request.setStatus(true);

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> productService.updateProduct(productId, request)
        );
        assertEquals("Producto no encontrado", exception.getMessage());
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, never()).save(any());
    }

    @Test
    void deleteProduct_WhenProductExists_SetsStatusFalse() {
        // Given
        Long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // When
        productService.deleteProduct(productId);

        // Then
        assertFalse(testProduct.isStatus());
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).save(testProduct);
    }

    @Test
    void deleteProduct_WhenProductNotExists_ThrowsRuntimeException() {
        // Given
        Long productId = 999L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> productService.deleteProduct(productId)
        );
        assertEquals("Producto no encontrado", exception.getMessage());
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, never()).save(any());
    }

    @Test
    void getFeaturedHomeProducts_ReturnsAllActiveProductsAsHomeDTOs() {
        // Given
        when(productRepository.findByStatusTrue()).thenReturn(List.of(testProduct));

        // When
        List<HomeProductDTO> result = productService.getFeaturedHomeProducts();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        HomeProductDTO dto = result.get(0);
        assertEquals(testProduct.getId(), dto.getId());
        assertEquals(testProduct.getName(), dto.getName());
        assertEquals(testProduct.getPrice(), dto.getPrice());
        assertEquals(testProduct.getImageUrl(), dto.getImageUrl());
        assertEquals(testCategory.getLabel(), dto.getCategoryLabel());
        verify(productRepository, times(1)).findByStatusTrue();
    }

    @Test
    void getFeaturedHomeProducts_WhenNoProducts_ReturnsEmptyList() {
        // Given
        when(productRepository.findByStatusTrue()).thenReturn(List.of());

        // When
        List<HomeProductDTO> result = productService.getFeaturedHomeProducts();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productRepository, times(1)).findByStatusTrue();
    }

    @Test
    void getFeaturedHomeProducts_WhenShortDescriptionIsNull_UsesDefaultDescription() {
        // Given
        testProduct.setShortDescription(null);
        when(productRepository.findByStatusTrue()).thenReturn(List.of(testProduct));

        // When
        List<HomeProductDTO> result = productService.getFeaturedHomeProducts();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getShortDescription().contains("ceramica artesanal"));
    }
}