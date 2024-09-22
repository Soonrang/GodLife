package com.example.rewardservice.challenge.application.response;


import com.example.rewardservice.challenge.domain.ChallengePost;

import java.time.LocalDate;
import java.util.UUID;

public record ChallengePostResponse(
        UUID postId,
        String title,
        String description,
        String imageUrl,
        LocalDate checkDate,
        String userNickname,
        String status
) {
    public static ChallengePostResponse from(ChallengePost challengePost) {
        return new ChallengePostResponse(
                challengePost.getId(),
                challengePost.getChallenge().getTitle(),
                challengePost.getDescription(),
                challengePost.getImage(),
                challengePost.getCreatedAt(),
                challengePost.getUser().getNickname(),
                challengePost.getStatus()
        );
    }
}
