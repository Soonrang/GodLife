package com.example.rewardservice.challenge.application.request;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ChallengeJoinRequest {

    private UUID challengeId;
    private long participationPoints;
}
