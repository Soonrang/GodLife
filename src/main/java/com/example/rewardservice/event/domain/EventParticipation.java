package com.example.rewardservice.event.domain;

import com.example.rewardservice.common.BaseEntity;
import com.example.rewardservice.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Table(name = "event_participation", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id")
})
public class EventParticipation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    private Long points;

    private String description;

    public EventParticipation(User user, Event event, Long pointEarned, String description) {
        this.user = user;
        this.event = event;
        this.points = pointEarned;
        this.description = description;
    }

    public EventParticipation(User user, Event event, long points) {
        this.user = user;
        this.event =event;
        this.points = points;
    }
}
