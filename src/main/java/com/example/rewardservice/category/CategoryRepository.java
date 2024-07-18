package com.example.rewardservice.category;

import com.example.rewardservice.shop.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    Boolean existsByName(String name);

}
