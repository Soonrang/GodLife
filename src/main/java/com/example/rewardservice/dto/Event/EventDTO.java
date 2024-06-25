package com.example.rewardservice.dto.Event;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class EventDTO {
    private UUID id;
    private String name;
    private String eventType;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createdAt;
    private LocalDateTime announcementDate;
    private String eventState;
    private int reward;
}
