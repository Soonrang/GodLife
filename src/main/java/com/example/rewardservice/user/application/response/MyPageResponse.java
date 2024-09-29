package com.example.rewardservice.user.application.response;

import com.example.rewardservice.challenge.application.response.UserResponse;
import com.example.rewardservice.user.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MyPageResponse {

    private final String email;
    private final String nickname;
    private final String name;
    private final long totalPoint;
    private final String profileImage;

    private final UserResponse challengeStats;

    public static MyPageResponse from(final User user,UserResponse challengeStats) {
        return new MyPageResponse(
                user.getEmail(),
                user.getNickname(),
                user.getName(),
                user.getTotalPoint(),
                user.getProfileImage(),
                challengeStats);
    }

}
