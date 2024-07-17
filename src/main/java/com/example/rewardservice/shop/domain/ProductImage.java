package com.example.rewardservice.shop.domain;

import jakarta.persistence.*;

import java.util.UUID;

public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;  // Link to the Product entity

    @Column(name = "image_url")
    private String imageUrl;
}
