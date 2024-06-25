package com.example.rewardservice.domain.Shop;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "product_category")
    private String category;

    //회사 계정을 따로 만들어 연결
    @Column(name = "product_company")
    private String company;

    @Column(name = "product_name")
    private String name;

    @Column(name = "product_price")
    private BigDecimal price;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> productImages;

    @Column(name= "product_description")
    private String description;

    @Column(name= "product_created_at")
    private LocalDateTime createdAt;

    @Column(name="product_last_update")
    private LocalDateTime lastUpdateDate;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

}
