package com.example.rewardservice.authentication.infrastructure;

import com.example.rewardservice.authentication.domain.TokenDecoder;
import com.example.rewardservice.authentication.domain.TokenType;

import java.util.Optional;

public class JwtDecoder implements TokenDecoder {
    @Override
    public Optional<PrivateClaims> decode(final TokenType tokenType,final String token) {
        return Optional.empty();
    }
}
