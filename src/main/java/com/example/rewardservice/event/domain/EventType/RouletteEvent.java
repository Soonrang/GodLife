package com.example.rewardservice.event.domain.EventType;

import com.example.rewardservice.event.domain.Event;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("ROULETTE")
@NoArgsConstructor
public class RouletteEvent extends Event {

    @Builder
    public RouletteEvent(String name, String description, LocalDateTime startDate, LocalDateTime endDate, String eventState, String eventType) {
        super(name, description, startDate, endDate, eventState, eventType);
    }

}
