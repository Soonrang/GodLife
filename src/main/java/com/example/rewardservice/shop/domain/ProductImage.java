package com.example.rewardservice.shop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;  // Link to the Product entity

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "upload_image_name")
    private String uploadName;

    public ProductImage(UUID uuid, String storeName, Product product) {
        this.id = uuid;
        this.imageUrl = storeName;
        this.product = product;
    }

    public ProductImage(UUID uuid, String imageUrl){
        this.id = uuid;
        this.imageUrl = imageUrl;
    }

}
