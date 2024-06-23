package com.example.rewardservice.dto.Point;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PointDTO {
    private UUID id;
    private String userId;
    private long totalPoint;
    private LocalDateTime lastUpdateDate;
}
