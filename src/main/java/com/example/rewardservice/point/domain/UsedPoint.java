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

    private static final String USED_REWARD = "사용";


    @Builder
    public UsedPoint(Product product, User user, Point point, long pointChange, String description, String pointType) {
        super(product, user, point, pointChange, description, USED_REWARD);
    }
}
