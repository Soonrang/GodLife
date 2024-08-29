package com.example.rewardservice.admin.presentation;

import com.example.rewardservice.admin.application.AdminProductService;
import com.example.rewardservice.admin.application.dto.request.ProductRegisterRequest;
import com.example.rewardservice.shop.application.response.ProductInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/shop")
@RequiredArgsConstructor
public class AdminProductController {

    private final AdminProductService adminProductService;

    @PostMapping("/product/register")
    public ResponseEntity<ProductInfoResponse>  registerProduct(@ModelAttribute ProductRegisterRequest productRegisterRequest) {
        ProductInfoResponse createdProduct = adminProductService.createEasyProduct(productRegisterRequest);
        return ResponseEntity.ok(createdProduct);
    }

   /* @PutMapping("/{productId}")
    public ResponseEntity<ProductInfoResponse> updateProduct(@PathVariable UUID productId,
                                                             @ModelAttribute UpdateProductRequest updateProductRequest) {
        ProductInfoResponse updatedProduct = adminProductService.updateProduct(productId, updateProductRequest);
        return ResponseEntity.ok(updatedProduct);
   }
    */

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID productId) {
        adminProductService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }


}
