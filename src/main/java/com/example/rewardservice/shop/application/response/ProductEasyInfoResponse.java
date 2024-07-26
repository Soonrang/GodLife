package com.example.rewardservice.shop.application.response;

import com.example.rewardservice.shop.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class ProductEasyInfoResponse {
    private UUID id;
    private String productName;
    private long price;
    private List<ProductImageResponse> productImages;

    public static ProductEasyInfoResponse from(Product product) {
        return new ProductEasyInfoResponse(
                product.getId(),
                product.getProductName(),
                product.getPrice(),
                product.getProductImages().stream().map(ProductImageResponse::from).collect(Collectors.toList())
        );
    }
}
