package com.example.rewardservice.shop.presentation;

import com.example.rewardservice.security.jwt.JwtTokenExtractor;
import com.example.rewardservice.shop.application.request.PurchaseRequest;
import com.example.rewardservice.shop.application.response.ProductInfoResponse;
import com.example.rewardservice.shop.application.response.ProductPagingResponse;
import com.example.rewardservice.shop.application.response.PurchaseDetailResponse;
import com.example.rewardservice.shop.application.response.PurchaseRecordResponse;
import com.example.rewardservice.shop.application.service.ProductService;
import com.example.rewardservice.shop.application.service.PurchaseService;
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
    private final PurchaseService purchaseService;

    @PostMapping("/order")
    public ResponseEntity<Void> purchaseProduct(@RequestBody PurchaseRequest purchaseRequest) {
        String userEmail = jwtTokenExtractor.getCurrentUserEmail();
        purchaseService.purchaseProduct(userEmail,purchaseRequest);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductInfoResponse> getProductInfo(@PathVariable UUID productId) {
        ProductInfoResponse product = productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }

    @GetMapping()
    public ResponseEntity<List<ProductInfoResponse>> getAllProducts() {
        List<ProductInfoResponse> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<ProductPagingResponse>> getPagedProducts(Pageable pageable) {
        Page<ProductPagingResponse> pagedProducts = productService.paging(pageable);
        return ResponseEntity.ok(pagedProducts);
    }


    //-----------------------구매
    @GetMapping("/purchases")
    public ResponseEntity<List<PurchaseDetailResponse>> getAllPurchases() {
        String email = jwtTokenExtractor.getCurrentUserEmail();
        List<PurchaseDetailResponse> purchases = purchaseService.viewAllPurchases(email);
        return ResponseEntity.ok(purchases);
    }

    // 개별 구매 내역 조회
    @GetMapping("/purchases/{id}")
    public ResponseEntity<PurchaseDetailResponse> getPurchaseDetail(@PathVariable UUID id) {
        PurchaseDetailResponse purchaseDetail = purchaseService.viewPurchaseDetails(id);
        return ResponseEntity.ok(purchaseDetail);
    }
}
