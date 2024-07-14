package com.example.rewardservice.point.domain;

import com.example.rewardservice.common.BaseEntity;
import com.example.rewardservice.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class PointLog extends BaseEntity {

    private static final String USE_POINT = "사용";
    private static final String EARN_POINT = "적립";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    //user와 userpoint
    @ManyToOne
    @JoinColumn(name = "user_email", nullable = false, updatable = false)
    private User user;

    @Column(name = "transaction_type")
    private String transactionType;

    @Column(name = "point_change")
    private long pointChange;

    @Column(name = "description")
    private String description;

    public long updateUserTotalPoints(long pointChange, String transactionType){
        validateType(transactionType);
        long curPoints = user.getTotalPoint();
        if(transactionType.equals(USE_POINT)){
            validateUsePoint(pointChange);
            curPoints -= pointChange;
        } else if (transactionType.equals(EARN_POINT)) {
            curPoints += pointChange;
        }
        user.setTotalPoint(curPoints);
        return curPoints;
    }

    private void validateType(String transactionType){
        if(!transactionType.equals(USE_POINT) && !transactionType.equals(EARN_POINT)){
            throw new IllegalArgumentException("Invalid transaction type");
        }
    }

    private void validateUsePoint(long points){
        if(user.getTotalPoint() < points) {
            throw new IllegalArgumentException("insufficient points");
        }
    }





}
