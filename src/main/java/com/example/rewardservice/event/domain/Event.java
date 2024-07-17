package com.example.rewardservice.event.domain;

import com.example.rewardservice.point.domain.Point;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "event_type")
public abstract class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "event_id")
    private UUID id;

    @Column(name = "event_name")
    private String name;

    @Column(name = "event_description")
    private String description;

    @Column(name = "even_create_date")
    private LocalDateTime createdAt;

    @Column(name = "event_start_date")
    private LocalDateTime startDate;

    @Column(name = "event_end_date")
    private LocalDateTime endDate;

    @Column(name = "event_announcement_date")
    private LocalDateTime announcementDate;

    @Column(name = "event_state")
    private String eventState;

    @Column(name = "event_reward")
    private int reward;

    @OneToMany(mappedBy = "event")
    private List<Point> points;



    public void updateEvent(String name,
                            String description,
                            LocalDateTime startDate,
                            LocalDateTime endDate,
                            String eventState
    ) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.eventState = eventState;
    }

}
