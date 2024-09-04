package com.example.rewardservice.challenge.application.request;

import com.example.rewardservice.challenge.domain.Challenge;
import com.example.rewardservice.challenge.domain.vo.*;
import org.springframework.web.multipart.MultipartFile;

public record ChallengeUpdateRequest(
        Title title,
        Category category,
        Span span,
        ParticipantsLimit participantsLimit,
        MultipartFile mainImage,
        MultipartFile successImage,
        MultipartFile failImage,
        Description description,
        AuthMethod authMethod
) {
    public void updateEntity(Challenge challenge, Images updatedImages) {
        challenge.update(
                title,
                category,
                span,
                participantsLimit,
                updatedImages,
                description,
                authMethod
        );
    }

}