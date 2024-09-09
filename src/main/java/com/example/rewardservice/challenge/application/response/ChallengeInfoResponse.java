package com.example.rewardservice.challenge.application.response;

import com.example.rewardservice.challenge.domain.Challenge;
import com.example.rewardservice.user.domain.User;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;


public record ChallengeInfoResponse(
        UUID id,
        String title,
        String category,
        LocalDate startDate,
        LocalDate endDate,
        LocalTime uploadStartTime,
        LocalTime uploadEndTime,
        long participantsLimit,
        String description,
        String authMethod,
        String mainImage,
        String successImage,
        String failImage,
        String userNickname,
        long participants,
        String isJoined
) {
    public static ChallengeInfoResponse from(Challenge challenge, String isJoined) {
        return new ChallengeInfoResponse(
                challenge.getId(),
                challenge.getTitle(),
                challenge.getCategory(),
                challenge.getChallengePeriod().getStartDate(),
                challenge.getChallengePeriod().getEndDate(),
                challenge.getChallengePeriod().getUploadStartTime(),
                challenge.getChallengePeriod().getUploadEndTime(),
                challenge.getParticipantsLimit(),
                challenge.getDescription(),
                challenge.getAuthMethod(),
                challenge.getChallengeImages().getMainImage(),
                challenge.getChallengeImages().getSuccessImage(),
                challenge.getChallengeImages().getFailImage(),
                challenge.getUser().getNickname(),
                challenge.getParticipantsLimit(),
                isJoined
        );
    }

    public static ChallengeInfoResponse from2(Challenge challenge, User user, String isJoined) {
        return new ChallengeInfoResponse(
                challenge.getId(),
                challenge.getTitle(),
                challenge.getCategory(),
                challenge.getChallengePeriod().getStartDate(),
                challenge.getChallengePeriod().getEndDate(),
                challenge.getChallengePeriod().getUploadStartTime(),
                challenge.getChallengePeriod().getUploadEndTime(),
                challenge.getParticipantsLimit(),
                challenge.getDescription(),
                challenge.getAuthMethod(),
                challenge.getChallengeImages().getMainImage(),
                challenge.getChallengeImages().getSuccessImage(),
                challenge.getChallengeImages().getFailImage(),
                challenge.getUser().getNickname(),
                challenge.getParticipantsLimit(),
                isJoined
        );
    }
}
