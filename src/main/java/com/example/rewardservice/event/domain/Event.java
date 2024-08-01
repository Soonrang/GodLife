package com.example.rewardservice.event.domain;

import com.example.rewardservice.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "event")
public class Event extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "event_id")
    private UUID id;

    @Column(name = "event_name")
    private String name;

    @Column(name = "event_state")
    private String eventState;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type")
    private EventType eventType;

    @Embedded
    private EventPeriod eventPeriod;

    public Event(String name, String eventState, EventType eventType, EventPeriod eventPeriod) {
        this.name = name;
        this.eventState = eventState;
        this.eventType = eventType;
        this.eventPeriod = eventPeriod;
    }

    public void updateEvent(String name, String eventState, EventType eventType, EventPeriod eventPeriod) {
        this.name = name;
        this.eventState = eventState;
        this.eventType = eventType;
        this.eventPeriod = eventPeriod;
    }
}