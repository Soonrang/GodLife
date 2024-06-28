package com.example.rewardservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private UUID id;
    private String category;
    private String companyName;
    private String productName;
    private BigDecimal price;
    private List<ProductImageDTO> productImages;
    private String description;
}
