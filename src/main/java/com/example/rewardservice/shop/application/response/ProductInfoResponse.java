package com.example.rewardservice.shop.application.response;

import com.example.rewardservice.shop.domain.Product;
import com.example.rewardservice.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class ProductInfoResponse {
    private UUID id;
    private String name;
    private long price;
    private String category;
    private int stock;
    private List<ProductImageResponse> productImages;
    private byte[] productImageData;

    public static ProductInfoResponse from(Product product, byte[] productImageData) {
        return new ProductInfoResponse(
                product.getId(),
                product.getProductName(),
                product.getPrice(),
                product.getCategory(),
                product.getStock(),
                product.getProductImages().stream().map(ProductImageResponse::from).collect(Collectors.toList()),
                productImageData
        );
    }
}
