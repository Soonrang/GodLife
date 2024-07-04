package com.example.rewardservice.authentication.infrastructure;

import com.example.rewardservice.authentication.domain.exception.UnsupportedSocialLoginException;

import java.util.Locale;

public enum Oauth2Type {

    KAKAO;

    public static Oauth2Type from(final String typeName) {
        try {
            return Oauth2Type.valueOf(typeName.toUpperCase(Locale.ENGLISH));
        }catch (final IllegalStateException ex) {
            throw new UnsupportedSocialLoginException("지원하는 소셜 로그인 기능이 아닙니다.");
        }
    }

    public String calculateNickname(final String name) {
        final String type = this.name().toLowerCase(Locale.ENGLISH);

        return type + name;
    }
}
