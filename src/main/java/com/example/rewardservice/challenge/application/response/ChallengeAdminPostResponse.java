package com.example.rewardservice.challenge.application.response;

import com.example.rewardservice.challenge.domain.Challenge;
import com.example.rewardservice.challenge.domain.ChallengePost;

import java.util.UUID;

public record ChallengeAdminPostResponse(
        String title,
        String mainImage,

        //참여한 유저 post
        UUID postId,
        String imageUrl,
        String description,
        String status,
        String userName,
        String userNickname
) {

    public static ChallengeAdminPostResponse from(Challenge challenge, ChallengePost challengePost) {
        return new ChallengeAdminPostResponse(
                challenge.getTitle(),
                challenge.getChallengeImages().getMainImage(),
                challengePost.getId(),
                challengePost.getImage(),
                challengePost.getDescription(),
                challengePost.getStatus(),
                challengePost.getUser().getName(),
                challengePost.getUser().getNickname()
        );
    }



}
