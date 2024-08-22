package com.example.rewardservice.shop.application.request;

import com.example.rewardservice.shop.domain.Product;

import java.util.UUID;

import java.util.List;
import java.util.UUID;

public record PurchaseRequest(List<ProductItem> products, long total, String userId) {

    public static record ProductItem(UUID id, int quantity, long price) {
    }
}
