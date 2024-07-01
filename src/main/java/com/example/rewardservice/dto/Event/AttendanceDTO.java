package com.example.rewardservice.dto.Event;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceDTO {
    private String userId;
    private String eventId;
    private LocalDateTime createdAt;
}
