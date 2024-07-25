package com.example.rewardservice.event.domain;

import com.example.rewardservice.common.BaseEntity;
import com.example.rewardservice.point.domain.Point;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Event extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "event_id")
    private UUID id;

    @Column(name = "event_name")
    private String name;

    @Column(name = "event_state")
    private String eventState;

    @Column(name = "event_type", insertable = false, updatable = false)
    private EventType eventType;

    @Embedded
    private EventPeriod  eventPeriod;

//    @OneToMany(mappedBy = "event")
//    private List<Point> points;

    public Event(String name, String eventState,EventType eventType, EventPeriod eventPeriod) {
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
