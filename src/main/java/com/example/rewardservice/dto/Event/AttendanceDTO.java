package com.example.rewardservice.dto.Event;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class AttendanceDTO {
    private String message;
    private long rewardPoints;
}
