package com.example.rewardservice.event.application.request;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class AddPointRequest {
    private String userEmail;
    private long points;
    private String description;
    private UUID activityId;
}
