package com.example.rewardservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private UUID id;
    private String category;
    private UUID companyId;
    private String productName;
    private BigDecimal price;
    private List<ProductImageDTO> productImages;
    private String description;
    private String createdAt;
}
