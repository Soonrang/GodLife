package com.example.rewardservice.user.application.dto.response;

import com.example.rewardservice.user.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class WriterResponse {

    private final String nickname;
    private final String profileImage;

    public static WriterResponse of(final User user) {
        return new WriterResponse(user.getNickname(), user.getProfileImage());
    }
}
