package com.example.rewardservice.event.domain.Events;

import com.example.rewardservice.common.BaseEntity;
import com.example.rewardservice.event.domain.Event;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@NoArgsConstructor
public class AttendanceEvent extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "attendance_event_id", columnDefinition = "BINARY(16)")
    private UUID attendanceEventId;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    private boolean bonusAwarded;


    public AttendanceEvent(Event event, boolean bonusAwarded) {
        this.event = event;
        this.bonusAwarded = bonusAwarded;
    }

}
