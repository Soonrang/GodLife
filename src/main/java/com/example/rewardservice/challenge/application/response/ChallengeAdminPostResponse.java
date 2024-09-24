package com.example.rewardservice.challenge.application.response;

import com.example.rewardservice.challenge.domain.Challenge;
import com.example.rewardservice.challenge.domain.ChallengePost;

import java.time.LocalDate;
import java.util.List;

public record ChallengeAdminPostResponse(
        String title,
        String mainImage,
        LocalDate startDate,
        LocalDate endDate,


        List<ChallengePostResponse> checkRecords
) {

    public static ChallengeAdminPostResponse from(Challenge challenge, List<ChallengePost> challengePosts) {
        List<ChallengePostResponse> postResponses = (challengePosts == null || challengePosts.isEmpty()) ?
                null : challengePosts.stream()
                .map(ChallengePostResponse::from)
                .toList();

        return new ChallengeAdminPostResponse(
                challenge.getTitle(),
                challenge.getChallengeImages().getMainImage(),
                challenge.getChallengePeriod().getStartDate(),
                challenge.getChallengePeriod().getEndDate(),
                postResponses
        );
    }



}
