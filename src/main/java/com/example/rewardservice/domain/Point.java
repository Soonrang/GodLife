package com.example.rewardservice.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Point{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "index")
    private int index;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "current_point", nullable = false)
    private long currentPoint;

    @Column(name = "last_update_date", nullable = false)
    private LocalDateTime lastUpdateDate;
}
