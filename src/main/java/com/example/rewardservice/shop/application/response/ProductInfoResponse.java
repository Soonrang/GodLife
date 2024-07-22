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
    private String category;
    private String companyName;
    private String productName;
    private long price;
    private List<ProductImageResponse> productImages;
    private String description;

    public static ProductInfoResponse from(Product product) {
        return new ProductInfoResponse(
                product.getId(),
                product.getCategory(), // Assuming Category has a getName() method
                product.getCompany().getName(),
                product.getProductName(),
                product.getPrice(),
                product.getProductImages().stream().map(ProductImageResponse::from).collect(Collectors.toList()),
                product.getDescription()
        );
    }
}
