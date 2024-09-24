package com.example.rewardservice.challenge.application.request;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record ChallengePostRequest(
        String description,
        MultipartFile image
) {
}
