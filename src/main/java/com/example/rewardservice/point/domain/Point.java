package com.example.rewardservice.point.domain;

import com.example.rewardservice.common.BaseEntity;
import com.example.rewardservice.donation.domain.DonationRecord;
import com.example.rewardservice.event.domain.Event;
import com.example.rewardservice.shop.domain.Product;
import com.example.rewardservice.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
public class Point extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "point_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "point_type", insertable = false, updatable = false)
    private String type;

    @Column(name = "point_amount")
    private long amount;

    @Column(name = "point_change_description")
    private String description;

    @Column(name = "point_deleted")
    private char deleted = NOT_DELETED;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "activity_type")
    private String activityType;

    @Column(name = "activity_id", columnDefinition = "BINARY(16)")
    private UUID activityId;

    public Point(User user, long amount, String description, String type, String activityType, UUID activityId) {
        this.user = user;
        this.amount = amount;
        this.description = description;
        this.type = type;
        this.activityType = activityType;
        this.activityId = activityId;
    }

    //이후 관리자 페이지에서 적용
    public static final char NOT_DELETED = 'N';
    public static final char DELETED = 'Y';

    public Point() {

    }

    public Point(Product product, User user, long point, String description, String pointTypeUsed) {

        super();
    }

    //소프트 딜리트 적용
    public void delete() {
        this.deleted = DELETED;
    }

}
