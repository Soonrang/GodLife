package com.example.rewardservice.point.application.dto;

import lombok.Getter;

@Getter
public class GiftPointRequest {
    private String senderEmail;
    private String receiverEmail;
    private long points;
    private String description;
}
