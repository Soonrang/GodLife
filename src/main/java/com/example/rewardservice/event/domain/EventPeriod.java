package com.example.rewardservice.event.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Getter;

import java.time.LocalDateTime;

@Embeddable
@Getter
public class EventPeriod {
    private LocalDateTime startDate;
    private LocalDateTime endDate;


    public EventPeriod() {
    }

    public EventPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }


}
