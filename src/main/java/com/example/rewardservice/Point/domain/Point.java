package com.example.rewardservice.Point.domain;

import com.example.rewardservice.Event.domain.Event;
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

    @ManyToOne
    @JoinColumn(name = "user_email", nullable = false)
    private User user;

    @Column(name = "point_created_at")
    private LocalDateTime createdAt;

    @Column(name = "point_change")
    private long pointChange;

    @Column(name = "point_change_description")
    private String description;


    public Point(Event event, User user, long pointChange, String description) {
        this.event = event;
        this.user = user;
        this.pointChange = pointChange;
        this.createdAt = LocalDateTime.now();
        this.description = description;
    }
}
