package com.example.rewardservice.point.domain;

import com.example.rewardservice.event.domain.Event;
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
@DiscriminatorValue("EARNED")
public class EarnedPoint extends Point {

    private String rewardType;
    private boolean isWinner;
    private int participationCount;

    @Builder
    public EarnedPoint(Event event, User user, long pointChange,
                       boolean isWinner, String rewardType, String description,
                       String pointType, int participationCount) {
        super(event, user, pointChange, description, pointType);
        this.rewardType = rewardType;
        this.isWinner = isWinner;
        this.participationCount = participationCount;
    }

}