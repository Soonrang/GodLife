package com.example.rewardservice.event.application.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddPointRequest {
    private String userEmail;
    private long points;
    private String description;
    private UUID activityId;
}
