package com.example.rewardservice.shop.presentation;

import com.example.rewardservice.auth.AuthUser;
import com.example.rewardservice.security.jwt.JwtTokenExtractor;
import com.example.rewardservice.shop.application.request.PurchaseRequest;
import com.example.rewardservice.shop.application.response.ProductEasyInfoResponse;
import com.example.rewardservice.shop.application.response.ProductInfoResponse;
import com.example.rewardservice.shop.application.response.ProductPagingResponse;
import com.example.rewardservice.shop.application.service.ProductService;
import com.example.rewardservice.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/shop")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final JwtTokenExtractor jwtTokenExtractor;

    @PostMapping("/product/purchase")
    public ResponseEntity<Void> purchaseProduct(@RequestParam UUID productId, @RequestBody PurchaseRequest purchaseRequest) {
        String userEmail = jwtTokenExtractor.getCurrentUserEmail();
        productService.purchaseProduct(userEmail, productId, purchaseRequest);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductInfoResponse> getProductInfo(@PathVariable UUID productId) {
        ProductInfoResponse product = productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductInfoResponse>> getAllProducts() {
        List<ProductInfoResponse> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping
    public ResponseEntity<Page<ProductPagingResponse>> getPagedProducts(Pageable pageable) {
        Page<ProductPagingResponse> pagedProducts = productService.paging(pageable);
        return ResponseEntity.ok(pagedProducts);
    }
}
