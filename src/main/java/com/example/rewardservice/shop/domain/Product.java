package com.example.rewardservice.shop.domain;

import com.example.rewardservice.common.BaseEntity;
import com.example.rewardservice.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name =  "product_category")
    private String category;

    //회사 계정을 따로 만들어 연결
    @ManyToOne
    @JoinColumn(name = "user_email")
    private User company;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_price")
    private long price;

    @Column(name = "product_stock")
    private int stock;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> productImages;

    @Column(name= "product_description")
    private String description;

    public Product(UUID uuid, String productName, long price, List<ProductImage> productImages) {
        this.id = uuid;
        this.productName = productName;
        this.price = price;
        this.productImages =productImages;
    }


    public void updateProduct(String category,
                              User company,
                              String productName,
                              String companyName,
                              long price,
                              int stock,
                              List<ProductImage> productImages,
                              String description){
        this.category = category;
        this.company = company;
        this.productName = productName;
        this.companyName = companyName;
        this.price = price;
        this.stock = stock;
        this.productImages = productImages;
        this.description = description;
    }

    public void reduceStock(int quantity) {
        if (this.stock < quantity) {
            throw new RuntimeException("재고가 부족합니다.");
        }
        this.stock -= quantity;
    }
}
