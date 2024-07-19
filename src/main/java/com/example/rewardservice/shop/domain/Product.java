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
    private UUID id;

    @Column(name =  "product_category")
    private String category;

    //회사 계정을 따로 만들어 연결
    @ManyToOne
    @JoinColumn(name = "user_eamil", nullable = false)
    private User company;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_price")
    private long price;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> productImages;

    @Column(name= "product_description")
    private String description;


    public void updateProduct(String category,
                              String productName,
                              String companyName,
                              long price,
                              List<ProductImage> productImages,
                              String description){
        this.category = category;
        this.productName = productName;
        this.category = companyName;
        this.price = price;
        this.productImages = productImages;
        this.description = description;
    }


}
