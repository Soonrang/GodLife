package com.example.rewardservice.challenge.application.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class ParticipantResponse {

    UUID userId;
    UUID userChallengeId;
    String email;
    String name;
    String nickname;
    double progress;
    LocalDateTime createdAt;

    public ParticipantResponse(UUID id, UUID id1, String email, String name, String nickname, double progress, LocalDateTime createdAt) {
        this.userId = id;
        this.userChallengeId = id1;
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.progress = progress;
        this.createdAt = createdAt;

    }
}
