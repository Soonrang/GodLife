package com.example.rewardservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProductImageDTO {
    private UUID id;
    private UUID productId;
    private String imageUrl;
}
