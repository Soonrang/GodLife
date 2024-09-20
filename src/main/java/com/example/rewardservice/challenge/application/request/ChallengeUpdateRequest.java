package com.example.rewardservice.challenge.application.request;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@Getter
public class ChallengeUpdateRequest {
    private String title;
    private String category;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime uploadStartTime;
    private LocalTime uploadEndTime;
    private long participantsLimit;
    private String description;
    private String authMethod;
    private MultipartFile mainImage;
    private MultipartFile successImage;
    private MultipartFile failImage;
}