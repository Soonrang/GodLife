package com.example.rewardservice.shop.application.request;

public record PurchaseRequest(String productName, long price, int quantity, long total) {
}

