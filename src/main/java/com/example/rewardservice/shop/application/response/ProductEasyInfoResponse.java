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
    private String productName;
    private String productCompany;
    private long productPrice;
    private String category;
    private int stock;
    private List<ProductImageResponse> productImages;

    public static ProductEasyInfoResponse from(Product product) {
        return new ProductEasyInfoResponse(
                product.getProductName(),
                product.getProductCompany(),
                product.getPrice(),
                product.getCategory(),
                product.getStock(),
                product.getProductImages().stream().map(ProductImageResponse::from).collect(Collectors.toList())
        );
    }
}
