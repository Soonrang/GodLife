package com.example.rewardservice.shop.application.request;

import lombok.Data;

import java.util.List;

@Data
public class MultiPurchaseRequest {
    private List<ProductDto> products;
    private double totalPrice;
    private String userId;

    @Data
    public static class ProductDto {
        private String id;
        private String productName;
        private double price;
        private String category;
        private int stock;
        private int quantity;
        private double total;
    }
}
