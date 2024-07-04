package com.example.rewardservice.authentication.infrastructure;

import com.example.rewardservice.authentication.domain.dto.UserInformationDto;

public interface OAuth2UserInformationProvider {

    Oauth2Type supportsOauth2Type();

    UserInformationDto findUserInformation(final String accessToken);

    UserInformationDto unlinkUserBy(final String oauthId);
}
