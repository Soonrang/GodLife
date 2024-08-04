package com.example.rewardservice.shop.domain.repository;

import com.example.rewardservice.shop.domain.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductImageRepository extends JpaRepository<ProductImage, UUID> {
}
