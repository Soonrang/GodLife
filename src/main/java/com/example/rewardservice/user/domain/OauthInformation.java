package com.example.rewardservice.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embedded
@NoArgsConstructor
@Getter
@ToString
public class OauthInformation {

    private String oauthId;

    @Column(name = "oauth2_type")
    @Enumerated(EnumType.STRING)
    private Oauth2Type oauth2Type;

    public OauthInformation(final String oauthId, final Oauth2Type oauth2Type) {
        this.oauthId = oauthId;
        this.oauth2Type = oauth2Type;
    }
}
