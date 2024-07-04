package com.example.rewardservice.authentication.domain;

import com.example.rewardservice.authentication.infrastructure.PrivateClaims;

import java.util.Optional;

public interface TokenDecoder {

    Optional<PrivateClaims> decode(final TokenType tokenType, final String token);
}
