package com.example.rewardservice.challenge.application.request;

import com.example.rewardservice.challenge.domain.Challenge;
import com.example.rewardservice.challenge.domain.vo.*;
import com.example.rewardservice.user.domain.User;
import org.springframework.web.multipart.MultipartFile;

public record ChallengeCreateRequest(
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

    public Challenge toEntity(User user, String mainImageUrl, String successImageUrl, String failImageUrl) {
        Images images = new Images(mainImageUrl, successImageUrl, failImageUrl);

        return new Challenge(
                user,
                title,
                category,
                span,
                participantsLimit,
                images,
                description,
                authMethod
        );
    }
}
