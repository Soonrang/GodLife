package com.example.rewardservice.user.dto;

import com.example.rewardservice.user.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MyPageResponse {

    private final String nickname;

    private final String profileImageUrl;

    public static MyPageResponse from(final User user) {
        return new MyPageResponse(user.getNickName(), user.getProfileImageUrl());
    }

}
