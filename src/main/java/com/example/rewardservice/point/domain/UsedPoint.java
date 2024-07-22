package com.example.rewardservice.point.domain;

import com.example.rewardservice.event.domain.Event;
import com.example.rewardservice.shop.domain.Product;
import com.example.rewardservice.user.domain.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("USED")
public class UsedPoint extends Point {


    @Builder
    public UsedPoint(Product product, User user, long pointChange, String description, String pointType) {
        super(product, user, pointChange, description,pointType);
    }
}
