package com.example.rewardservice.point.domain;

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
@DiscriminatorColumn(name = "point_type")
public abstract class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "point_id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(name = "point_type")
    private String pointType;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_email", nullable = false)
    private User user;

    @Column(name = "point_change")
    private long pointChange;

    @Column(name = "point_change_description")
    private String description;


    public Point(Event event, User user, long pointChange, String description, String pointType) {
        this.event = event;
        this.user = user;
        this.pointChange = pointChange;
        this.description = description;
        this.pointType = pointType;
    }

    public Point(Product product, User user, Point point, long pointChange, String description, String usedReward) {
        this.product = product;
        this.user = user;
        this.pointChange = pointChange;
        this.description = description;
        this.pointType = pointType;
    }
}
