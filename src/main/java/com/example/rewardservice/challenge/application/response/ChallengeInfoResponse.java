package com.example.rewardservice.challenge.application.response;

import com.example.rewardservice.challenge.domain.Challenge;

import java.util.UUID;


public record ChallengeInfoResponse(
        UUID id,
        String title,
        String category,
        String startDate,
        String endDate,
        long participantsLimit,
        String description,
        String authMethod,
        String mainImage,
        String successImage,
        String failImage,
        String userNickname
) {
    public static ChallengeInfoResponse from(Challenge challenge) {
        return new ChallengeInfoResponse(
                challenge.getId(),
                challenge.getTitle(),
                challenge.getCategory(),
                challenge.getStartDate().toString(),
                challenge.getEndDate().toString(),
                challenge.getParticipantsLimit(),
                challenge.getDescription(),
                challenge.getAuthMethod(),
                challenge.getMainImage(),
                challenge.getSuccessImage(),
                challenge.getFailImage(),
                challenge.getUser().getNickname()
        );
    }
}
