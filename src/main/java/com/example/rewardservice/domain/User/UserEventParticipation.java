package com.example.rewardservice.domain.User;

import com.example.rewardservice.domain.Point.Point;
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
public class UserEventParticipation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Point user;

    @ManyToOne
    @JoinColumn(name="event_id", nullable = false)
    private Event event;

    private LocalDateTime participatedAt;

}
