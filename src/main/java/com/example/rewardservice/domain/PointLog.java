package com.example.rewardservice.domain;

import com.example.rewardservice.domain.User.User;
import jakarta.persistence.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
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
