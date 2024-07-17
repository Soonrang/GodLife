package com.example.rewardservice.shop.application.dto;

import java.util.List;
import java.util.UUID;

public class RegisterProductRequest {
    private UUID id;
    private String category;
    private UUID companyId;
    private String productName;
    private long price;
    private List<ProductImageDto> productImages;
    private String description;
    private String createdAt;
}
