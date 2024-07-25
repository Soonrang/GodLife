package com.example.rewardservice.point.domain;

import com.example.rewardservice.common.BaseEntity;
import com.example.rewardservice.event.domain.Event;
import com.example.rewardservice.shop.domain.Product;
import com.example.rewardservice.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "point_types")
public abstract class Point extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "point_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @Column(name = "point_type", insertable = false, updatable = false)
    private String pointType;

    @ManyToOne
    @JoinColumn(name = "product_id", columnDefinition = "BINARY(16)")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_email")
    private User user;

    @Column(name = "point_change")
    private long pointChange;

    @Column(name = "point_change_description")
    private String description;

    @Column(name = "deleted_at")
    private String deletedAt;



    public Point(Event event, User user, long pointChange, String description, String pointType) {
        this.event = event;
        this.user = user;
        this.pointChange = pointChange;
        this.description = description;
        this.pointType = pointType;
        this.deletedAt = "N";
    }


    //이후 관리자 페이지에서 적용
    public static final String NOT_DELETED = "N";
    public static final String DELETED = "Y";

    public Point(Product product, User user, long pointChange, String description, String pointType) {
        this.product = product;
        this.user = user;
        this.pointChange = pointChange;
        this.description = description;
        this.pointType = pointType;
    }


    //소프트 딜리트 적용
    public void delete() {
        this.deletedAt = "Y";
    }

}
