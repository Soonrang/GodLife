package com.example.rewardservice.dto.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserEventParticipationDTO {
    private UUID id;
    private UUID userId;
    private UUID eventId;
    private LocalDateTime participatedAt;
}
