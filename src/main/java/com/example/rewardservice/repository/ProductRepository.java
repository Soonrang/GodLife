package com.example.rewardservice.repository;

import com.example.rewardservice.domain.Shop.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {


}
