package com.example.rewardservice.point.application;

import com.example.rewardservice.point.domain.Point;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class PointRecordResponse {

    private String type;
    private String description;
    private long points;
    private LocalDateTime createdAt;

    public static PointRecordResponse from(Point point) {
        return PointRecordResponse.builder()
                .type(point.getType())
                .description(point.getDescription())
                .points(point.getAmount())
                .createdAt(point.getCreatedAt())
                .build();
    }
}
