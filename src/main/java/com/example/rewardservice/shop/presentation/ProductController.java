package com.example.rewardservice.shop.presentation;

import com.example.rewardservice.auth.AuthUser;
import com.example.rewardservice.shop.application.request.RegisterProductRequest;
import com.example.rewardservice.shop.application.request.UpdateProductRequest;
import com.example.rewardservice.shop.application.response.ProductInfoResponse;
import com.example.rewardservice.shop.application.service.ProductService;
import com.example.rewardservice.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/product/register")
    public ResponseEntity<ProductInfoResponse> getProduct(@AuthUser User company,
                                                          @ModelAttribute RegisterProductRequest registerProductRequest) {
        ProductInfoResponse createdProduct = productService.createProduct(company, registerProductRequest);
        return ResponseEntity.ok(createdProduct);
    }

    @PostMapping("/product/purchase")
    public ResponseEntity<Void> purchaseProduct(@AuthUser User user, @RequestParam UUID productId) {
        productService.purchaseProduct(user.getEmail(), productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductInfoResponse> getProductInfo(@PathVariable UUID productId) {
        ProductInfoResponse product = productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }

    @GetMapping
    public ResponseEntity<List<ProductInfoResponse>> getAllProducts() {
        List<ProductInfoResponse> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductInfoResponse> updateProduct(@PathVariable UUID productId,
                                                             @ModelAttribute UpdateProductRequest updateProductRequest) {
        ProductInfoResponse updatedProduct = productService.updateProduct(productId, updateProductRequest);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

}
