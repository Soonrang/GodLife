package com.example.rewardservice.challenge.application.response;

import com.example.rewardservice.challenge.domain.Challenge;

public record UserResponse(long userTotalPrize,long ongoingChallenges,long endChallenges,long createdChallenges) {

    public UserResponse(long userTotalPrize, long ongoingChallenges, long endChallenges, long createdChallenges ) {
        this.userTotalPrize = userTotalPrize;
        this.ongoingChallenges = ongoingChallenges;
        this.endChallenges = endChallenges;
        this.createdChallenges = createdChallenges;
    }
}
