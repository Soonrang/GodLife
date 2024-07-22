package com.example.rewardservice.point.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class AddPointRequest {

    private UUID eventId;
    private String userEmail;
    private long pointChange;
    private String description;
    private boolean isWinner;
    private String rewardType;
    private String pointType;
    private int participationCount;

}
