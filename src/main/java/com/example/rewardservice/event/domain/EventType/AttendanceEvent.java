package com.example.rewardservice.event.domain.EventType;

import com.example.rewardservice.event.domain.Event;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@DiscriminatorValue("ATTENDANCE")
@NoArgsConstructor
public class AttendanceEvent extends Event {
    @Override
    public void updateEvent(String name, String description, LocalDateTime startDate, LocalDateTime endDate, String eventState) {
        this.setName(name);
        this.setDescription(description);
        this.setStartDate(startDate);
        this.setEndDate(endDate);
        this.setEventState(eventState);
    }

    @Builder
    public AttendanceEvent(String name, String description, LocalDateTime startDate, LocalDateTime endDate, String eventState, String eventType) {
        super(name, description, startDate, endDate, eventState,eventType);
    }
}
