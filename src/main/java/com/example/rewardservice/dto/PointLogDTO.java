package com.example.rewardservice.dto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointLogDTO {
    private UUID id;
    private UUID userId;
    private String transactionType;
    private long pointChange;
    private String description;
    private LocalDateTime createdAt;


}
