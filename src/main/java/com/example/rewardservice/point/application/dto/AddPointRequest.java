package com.example.rewardservice.point.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class AddPointRequest {

    private UUID eventId;
    private String userEmail;
    private long point;
    private String description;
    private String rewardType;
    private String pointType;

}
