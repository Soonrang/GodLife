package com.example.rewardservice.shop.application.request;

import com.example.rewardservice.shop.domain.Product;

import java.util.UUID;

public record PurchaseRequest(UUID id, long price, int quantity, long total, String userId, String name, String category, String productImages) {
}
