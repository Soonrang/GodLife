package com.example.rewardservice.security.oAuth;

import lombok.Getter;

@Getter
public class KakaoResponse {
    String grant_type;
    String client_id;
    String redirect_uri;
    String code;
    String client_secret;
}
