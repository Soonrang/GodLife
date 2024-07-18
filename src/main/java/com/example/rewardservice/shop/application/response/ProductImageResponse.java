package com.example.rewardservice.shop.application.response;

import com.example.rewardservice.shop.domain.ProductImage;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class ProductImageResponse {
    private UUID id;
    private String imageUrl;

    public static ProductImageResponse from(ProductImage productImage) {
        return new ProductImageResponse(
                productImage.getId(),
                productImage.getImageUrl()
        );
    }


}
