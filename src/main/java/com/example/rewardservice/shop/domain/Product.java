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
    private String productCompany;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_price")
    private long price;

    @Column(name = "product_stock")
    private int stock;

    @Column(name = "product_status")
    private String status;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> productImages;

    @Column(name= "product_description")
    private String description;

    public Product(UUID uuid,
                   String productCompany,
                   String productName,
                   long price,
                   String category,
                   int stock,
                   List<ProductImage> productImages,
                   String status) {
        this.id = uuid;
        this.productCompany = productCompany;
        this.productName = productName;
        this.price = price;
        this.category = category;
        this.stock = stock;
        this.productImages =productImages;
        this.status = status;
    }

    public void updateProduct(String category,
                              String productName,
                              long price,
                              int stock,
                              List<ProductImage> productImages,
                              String description,
                              String status){
        this.category = category;
        this.productName = productName;
        this.price = price;
        this.stock = stock;
        this.productImages = productImages;
        this.description = description;
        this.status = status;
    }

    public void reduceStock(int quantity) {
        if (this.stock < quantity) {
            throw new RuntimeException("재고가 부족합니다.");
        }
        this.stock -= quantity;
    }

    public List<ProductImage> getProductImages() {
        return productImages;
    }

    public void minusPurchaseQuantity(int purchaseQuantity) {
        this.stock -= purchaseQuantity;
    }

    public void changeProductStatus(String newStatus) {
        this.status = newStatus;
    }


}
