package com.example.rewardservice.point.domain;

import com.example.rewardservice.common.BaseEntity;
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
    @JoinColumn(name = "product_id", columnDefinition = "BINARY(16)")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_email")
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    public Point(Event event, User user, long amount, String description, String pointType) {
        this.event = event;
        this.user = user;
        this.amount = amount;
        this.description = description;
        this.type = pointType;
    }

    public Point(Product product, User user, long pointChange, String description, String pointType) {
        this.product = product;
        this.user = user;
        this.amount = pointChange;
        this.description = description;
        this.type = pointType;
    }

    //이후 관리자 페이지에서 적용
    public static final char NOT_DELETED = 'N';
    public static final char DELETED = 'Y';

    public Point() {

    }

    //소프트 딜리트 적용
    public void delete() {
        this.deleted = DELETED;
    }

}
