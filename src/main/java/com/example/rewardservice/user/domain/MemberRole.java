package com.example.rewardservice.user.domain;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum MemberRole {
    USER("user"),
    ADMIN("admin");

    private final String value;

    MemberRole(String value) {
        this.value = value;
    }

    public static MemberRole from(String role) {
        return Arrays.stream(MemberRole.values())
                .filter(it -> it.value.equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(()-> new IllegalArgumentException("회원 Role 찾을 수 없음"));
    }
}
