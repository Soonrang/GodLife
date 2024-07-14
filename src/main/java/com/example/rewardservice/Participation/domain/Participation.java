package com.example.rewardservice.Participation.domain;

import com.example.rewardservice.Event.domain.Event;
import com.example.rewardservice.common.BaseEntity;
import com.example.rewardservice.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Participation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "participate_id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne
    @JoinColumn(name = "user_email", nullable = false)
    private User user;

    @Column(name = "participate_date")
    private LocalDateTime participationDate;

    private boolean isWinner;
    private long rewardAmount;
    private int monthlyParticipationCount;

}
