package com.example.rewardservice.dto.Event;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

public class EventDTO {
    private UUID id;
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createdDate;
    private LocalDateTime announcementDate;
}
