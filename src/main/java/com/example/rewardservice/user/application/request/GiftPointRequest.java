package com.example.rewardservice.user.application.request;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class GiftPointRequest {
    private String senderEmail;
    private String receiverEmail;
    private long points;
    private String description;
    private UUID activityId;

}
