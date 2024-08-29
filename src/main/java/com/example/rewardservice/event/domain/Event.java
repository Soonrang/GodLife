package com.example.rewardservice.event.domain;

import com.example.rewardservice.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
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

    @Column(name = "event_banner")
    private String eventImageBanner;

    @Column(name = "event_main")
    private String eventImageMain;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type")
    private EventType eventType;

    @Embedded
    private EventPeriod eventPeriod;

    public Event(String name, String eventState, EventType eventType, EventPeriod eventPeriod
    ,String imageMain, String imageBanner) {
        this.name = name;
        this.eventState = eventState;
        this.eventType = eventType;
        this.eventPeriod = eventPeriod;
        this.eventImageMain = imageMain;
        this.eventImageBanner = imageBanner;
    }

    public void updateEvent(String name, String eventState, EventType eventType, EventPeriod eventPeriod
            ,String imageMain, String imageBanner) {
        this.name = name;
        this.eventState = eventState;
        this.eventType = eventType;
        this.eventPeriod = eventPeriod;
        this.eventImageMain = imageMain;
        this.eventImageBanner = imageBanner;
    }

    public void changeEventStatus(String eventState){
        this.eventState = eventState;
    }
}