package com.example.rewardservice.point.application;

import lombok.Getter;

@Getter
public class GiftRequest {
    String recipientId;
    long points;
    String senderId;
}
