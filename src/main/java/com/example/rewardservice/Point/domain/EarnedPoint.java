package com.example.rewardservice.Point.domain;

import com.example.rewardservice.Event.domain.Event;
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

    private static final String EARNED_REWARD = "적립";

    private String rewardType;
    private boolean isWinner;
    private int participationCount;

    @Builder
    public EarnedPoint(Event event, User user, long pointChange, boolean isWinner, int participationCount, String description) {
        super(event, user, pointChange, description);
        this.rewardType = EARNED_REWARD;
        this.isWinner = isWinner;
        this.participationCount = participationCount;
    }

}
