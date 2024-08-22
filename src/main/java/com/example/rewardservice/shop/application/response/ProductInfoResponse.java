package com.example.rewardservice.shop.application.response;

import com.example.rewardservice.shop.domain.Product;
import com.example.rewardservice.shop.domain.ProductImage;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class ProductInfoResponse {
    private UUID id;
    private String productName;
    private long price;
    private String category;
    private int stock;
    private String productImages;

    public static ProductInfoResponse from(Product product) {
        String imageUrl = product.getProductImages().isEmpty() ? null : product.getProductImages().get(0).getImageUrl();
        return new ProductInfoResponse(
                product.getId(),
                product.getProductName(),
                product.getPrice(),
                product.getCategory(),
                product.getStock(),
                imageUrl
        );
    }
}

