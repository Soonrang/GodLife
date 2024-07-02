package com.example.rewardservice.dto.Event;

import com.example.rewardservice.domain.User.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceDTO {
    private UUID userId;
    private UUID eventId;
    private LocalDateTime createdAt;
}
