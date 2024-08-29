package com.example.rewardservice.event.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Embeddable
@Getter
public class EventPeriod {
    private LocalDate startDate;
    private LocalDate endDate;


    public EventPeriod() {
    }

    public EventPeriod(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }


}
