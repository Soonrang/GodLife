package com.example.rewardservice.shop.application.request;

import com.example.rewardservice.image.application.dto.StoreImageDto;
import com.example.rewardservice.shop.application.ProductImageDto;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class RegisterProductRequest {
    private UUID id;
    private String category;
    private String companyEmail;
    private String productName;
    private long price;
    private List<StoreImageDto> productImages;
    private String description;
    private String createdAt;
}
