package com.example.rewardservice.challenge.application.response;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UserChallengeInfoResponse {
    private UUID challengeId;
    private String title;
}
