package com.example.rewardservice.admin.presentation;

import com.example.rewardservice.admin.application.AdminProductService;
import com.example.rewardservice.admin.application.dto.ProductRegisterRequest;
import com.example.rewardservice.admin.application.dto.UpdateProductRequest;
import com.example.rewardservice.auth.AuthUser;
import com.example.rewardservice.shop.application.response.ProductInfoResponse;
import com.example.rewardservice.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/admin/shop")
@RequiredArgsConstructor
public class AdminProductController {

    private final AdminProductService adminProductService;

    @PostMapping("/product/register")
    public ResponseEntity<ProductInfoResponse> getProduct(@AuthUser User company,
                                                          @ModelAttribute ProductRegisterRequest productRegisterRequest) {
        ProductInfoResponse createdProduct = adminProductService.createProduct(company, productRegisterRequest);
        return ResponseEntity.ok(createdProduct);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductInfoResponse> updateProduct(@PathVariable UUID productId,
                                                             @ModelAttribute UpdateProductRequest updateProductRequest) {
        ProductInfoResponse updatedProduct = adminProductService.updateProduct(productId, updateProductRequest);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID productId) {
        adminProductService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }


}
