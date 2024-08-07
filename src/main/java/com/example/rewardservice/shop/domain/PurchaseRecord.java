package com.example.rewardservice.shop.domain;

import com.example.rewardservice.common.BaseEntity;
import com.example.rewardservice.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "purchase_record", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id")
})
public class PurchaseRecord extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "points_used")
    private long points;

    @Column(name = "product_quantity")
    private int productQuantity;

    public PurchaseRecord(User user, Product product, long points, int productQuantity) {
        this.user = user;
        this.product = product;
        this.points = points;
        this.productQuantity = productQuantity;
    }
}
