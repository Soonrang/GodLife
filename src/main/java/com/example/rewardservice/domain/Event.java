package com.example.rewardservice.domain;

import jakarta.persistence.Entity;
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
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;
    private String eventType;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime announcementDate;
    private String eventState;
    private int reward;
    private LocalDateTime createdAt;


    public void updateEvent(String name, String eventType, String description, LocalDateTime startDate, LocalDateTime endDate, String eventState, int reward) {
        this.name = name;
        this.eventType = eventType;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.eventState = eventState;
        this.reward = reward;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
