package com.example.rewardservice.dto.User;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {
    private UUID id;
    private String userId;
    private long totalPoint;
    private LocalDateTime lastUpdateDate;
}
