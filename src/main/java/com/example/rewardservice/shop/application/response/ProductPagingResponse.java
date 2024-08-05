package com.example.rewardservice.shop.application.response;

import com.example.rewardservice.shop.domain.Product;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ProductPagingResponse {

    private String category;
    private String productName;
    private long price;
    private int stock;
    private List<ProductImageResponse> productImages;
    private String description;

    public static ProductPagingResponse from(Product product) {
        return new ProductPagingResponse(
                product.getCategory(),
                product.getProductName(),
                product.getPrice(),
                product.getStock(),
                product.getProductImages().stream().map(ProductImageResponse::from).collect(Collectors.toList()),
                product.getDescription()
        );
    }



}
