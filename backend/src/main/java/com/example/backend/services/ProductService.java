package com.example.backend.services;

import com.example.backend.dto.ProductDetailResponse;
import com.example.backend.dto.RelatedProductDTO;
import com.example.backend.dto.SizeDTO;
import com.example.backend.models.Product;
import com.example.backend.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

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
}
