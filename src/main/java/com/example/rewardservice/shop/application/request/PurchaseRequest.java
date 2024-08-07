package com.example.rewardservice.shop.application.request;

public record PurchaseRequest(String name, long price, int quantity, long totalPrice) {
}

