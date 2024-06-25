package com.example.rewardservice.domain.Point;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Point{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "idx")
    private int index;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "current_point")
    private long totalPoint;

    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;



}
