package com.example.rewardservice.controller;

import com.example.rewardservice.domain.Shop.ProductImage;
import com.example.rewardservice.dto.ProductDTO;
import com.example.rewardservice.dto.ProductImageDTO;
import com.example.rewardservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/shop/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<ProductDTO> createProduct(@RequestParam String category,
                                                    @RequestParam UUID companyId,
                                                    @RequestParam String productName,
                                                    @RequestParam BigDecimal price,
                                                    @RequestBody List<ProductImageDTO> images,
                                                    @RequestParam String description) {
        ProductDTO productDTO = ProductDTO.builder()
                .category(category)
                .companyId(companyId)
                .productName(productName)
                .price(price)
                .productImages(images)
                .description(description)
                .build();

        ProductDTO createdProduct = productService.createProduct(productDTO);
        return ResponseEntity.ok(createdProduct);
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable UUID id) {
        ProductDTO product = productService.getProductById(id);
        if(product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable UUID id,
                                                    @RequestParam UUID companyId,
                                                    @RequestParam String category,
                                                    @RequestParam String productName,
                                                    @RequestParam BigDecimal price,
                                                    @RequestBody List<ProductImageDTO> images,
                                                    @RequestParam String description) {
        ProductDTO productDTO = ProductDTO.builder()
                .category(category)
                .companyId(companyId)
                .productName(productName)
                .price(price)
                .productImages(images)
                .description(description)
                .build();
        ProductDTO updateProduct = productService.updateProduct(id, productDTO);
        if(updateProduct == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updateProduct);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        boolean deleted = productService.deleteProduct(id);

        if(!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }


}
