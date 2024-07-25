package com.example.rewardservice.event.domain.Events;

import com.example.rewardservice.common.BaseEntity;
import com.example.rewardservice.event.domain.Event;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor
public class RouletteEvent extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "roulette_event_id", columnDefinition = "BINARY(16)")
    private UUID rouletteEventId;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    private long awardPoints;

    public RouletteEvent(Event event, long awardPoints) {
        this.event = event;
        this.awardPoints = awardPoints;
    }
}
