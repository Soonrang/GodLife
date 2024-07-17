package com.example.rewardservice.event.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UpdatePointRequest {
    private String userId;
    private long earnedPoints;
}
