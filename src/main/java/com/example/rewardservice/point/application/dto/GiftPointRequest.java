package com.example.rewardservice.point.application.dto;

import lombok.Getter;

@Getter
public class GiftPointRequest {
    private String senderId;
    private String recipientId;
    private long points;
    private String description;
}
