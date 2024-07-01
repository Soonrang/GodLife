package com.example.rewardservice.domain.Shop;

import com.example.rewardservice.domain.BaseEntity;
import com.example.rewardservice.domain.User.User;
import com.example.rewardservice.dto.ProductDTO;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
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

    @Column(name = "product_category")
    private String category;

    //회사 계정을 따로 만들어 연결
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private User company;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_price")
    private BigDecimal price;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> productImages;

    @Column(name= "product_description")
    private String description;


    public void updateProduct(ProductDTO productDTO, User company, List<ProductImage> productImages) {
        this.category = productDTO.getCategory();
        this.company = company;
        this.productName = productDTO.getProductName();
        this.price = productDTO.getPrice();
        this.description = productDTO.getDescription();
        this.productImages.clear();
        this.productImages.addAll(productImages);
    }

}
