package com.example.rewardservice.shop.domain.repository;

import com.example.rewardservice.shop.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
