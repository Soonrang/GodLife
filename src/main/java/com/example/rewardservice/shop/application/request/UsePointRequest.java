package com.example.rewardservice.shop.application.request;

import com.example.rewardservice.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class UsePointRequest {
    private String userEmail;
    private long points;
    private String description;
    private UUID activityId;
}
