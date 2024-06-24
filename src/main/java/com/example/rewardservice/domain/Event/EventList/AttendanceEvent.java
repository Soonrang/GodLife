package com.example.rewardservice.domain.Event.EventList;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class AttendanceEvent extends Event {
    private int dailyPoints = 100;
}
