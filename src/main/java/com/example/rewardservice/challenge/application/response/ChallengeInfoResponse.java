package com.example.rewardservice.challenge.application.response;

import com.example.rewardservice.challenge.domain.Challenge;
import com.example.rewardservice.user.domain.User;

import java.util.UUID;


public record ChallengeInfoResponse(
        UUID id,
        String title,
        String category,
        String startDate,
        String endDate,
        int participantsLimit,
        String description,
        String authMethod,
        User user
) {
    public static ChallengeInfoResponse from(Challenge challenge) {
        return new ChallengeInfoResponse(
                challenge.getId(),
                challenge.getTitle().getValue(),
                challenge.getCategory().getValue(),
                challenge.getSpan().getStartDate().toString(),
                challenge.getSpan().getEndDate().toString(),
                challenge.getParticipantsLimit().getValue(),
                challenge.getDescription().getValue(),
                challenge.getAuthMethod().getValue(),
                challenge.getUser()
        );
    }
}
