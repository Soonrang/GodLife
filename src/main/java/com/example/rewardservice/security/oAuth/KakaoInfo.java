package com.example.rewardservice.security.oAuth;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class KakaoInfo {
    private String id;
    private String nickname;
    private String profile_image_url;

    public KakaoInfo(String id, String nickname, String profile_image_url) {
        this.id = id;
        this.nickname = nickname;
        this.profile_image_url = profile_image_url;
    }
}
