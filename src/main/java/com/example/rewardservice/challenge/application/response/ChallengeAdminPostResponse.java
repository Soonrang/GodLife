package com.example.rewardservice.challenge.application.response;

import com.example.rewardservice.challenge.domain.Challenge;
import com.example.rewardservice.challenge.domain.ChallengePost;

import java.util.List;
import java.util.UUID;

public record ChallengeAdminPostResponse(
        String title,
        String mainImage,

        List<ChallengePostResponse> checkRecord
) {

    public static ChallengeAdminPostResponse from(Challenge challenge, List<ChallengePost> challengePosts) {
        List<ChallengePostResponse> postResponses = (challengePosts == null || challengePosts.isEmpty()) ?
                null : challengePosts.stream()
                .map(ChallengePostResponse::from)
                .toList();

        return new ChallengeAdminPostResponse(
                challenge.getTitle(),
                challenge.getChallengeImages().getMainImage(),
                postResponses
        );
    }



}
