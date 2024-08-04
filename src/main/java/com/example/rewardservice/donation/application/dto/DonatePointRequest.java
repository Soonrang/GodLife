package com.example.rewardservice.donation.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class DonatePointRequest {
    private String userEmail;
    private long points;
    private String description;
    private UUID activityId;
}
