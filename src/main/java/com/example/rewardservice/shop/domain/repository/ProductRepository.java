package com.example.rewardservice.shop.domain.repository;

import com.example.rewardservice.shop.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    Optional<Product> findById(UUID productId);
    Optional<Product> findByCategory(String category);
}
