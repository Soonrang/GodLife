package com.example.rewardservice.Event.domain.EventType;

import com.example.rewardservice.Event.domain.Event;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("ATTENDANCE")
public class AttendanceEvent extends Event {
    private int bonusPoints;
    @Override
    public void updateEvent(String name, String description, LocalDateTime startDate, LocalDateTime endDate, String eventState) {
        this.setName(name);
        this.setDescription(description);
        this.setStartDate(startDate);
        this.setEndDate(endDate);
        this.setEventState(eventState);
    }

    public void setBonusPoints(int bonusPoints) {
        this.bonusPoints = bonusPoints;
    }

    public int getBonusPoints() {
        return bonusPoints;
    }



}
