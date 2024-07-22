package com.example.rewardservice.point.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class UsePointRequest {
    String userEmail;
    UUID productId;
    long pointChange;
    String description;
}
