package com.example.rewardservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageDTO {
    private UUID id;
    private UUID productId;
    private String imageUrl;
}
