package com.example.rewardservice.dto.Point;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PointLogDTO {
    private UUID id;
    private UUID pointId;
    private String transactionType;
    private long pointChange;
    private String description;
    private LocalDateTime createdAt;
}
