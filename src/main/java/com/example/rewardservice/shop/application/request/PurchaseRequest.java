package com.example.rewardservice.shop.application.request;

import lombok.Getter;

@Getter
public record PurchaseRequest(String name, long price, int quantity, long totalPrice) {

}
