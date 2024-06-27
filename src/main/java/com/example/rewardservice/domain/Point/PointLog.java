package com.example.rewardservice.domain.Point;

import com.example.rewardservice.domain.BaseEntity;
import com.example.rewardservice.domain.User.User;
import jakarta.persistence.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "transaction_type")
    private String transactionType;

    @Column(name = "point_change")
    private long pointChange;

    @Column(name = "description")
    private String description;

}
