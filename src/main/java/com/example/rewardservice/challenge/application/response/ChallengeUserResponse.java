package com.example.rewardservice.challenge.application.response;

import com.example.rewardservice.challenge.domain.Challenge;
import com.example.rewardservice.challenge.domain.UserChallenge;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
public class ChallengeUserResponse {

    //챌린지 정보
    private UUID challengeId;
    private String title;
    private String category;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime uploadStartTime;
    private LocalTime uploadEndTime;
    private long participantsLimit;
    private String description;
    private String authMethod;

    // 챌린지 이미지
    private String mainImage;
    private String successImage;
    private String failImage;

    // 챌린지 업로드 유저
    private String uploadUserNickname;

    // 챌린지 총 참여 인원
    private long participants;

    // 챌린지 전체 상금
    private long totalPrize;

    // 챌린지 상태 (진행전, 진행중, 종료)
    private String state;

    // 상금 정보 매핑 (챌린지 종료시에만 반환)
    private long prize;

    // 챌린지 최종 종료 여부
    @JsonProperty("isClosed")
    private boolean isClosed;

    // 챌린지 달성률
    private double progress;

    //참가유저정보
    private String participantNickname;
    private String participantName;
    private String participantUuid;
    private String participantEmail;
    private long deposit;

    public static ChallengeUserResponse from(Challenge challenge, UserChallenge userChallenge) {
        ChallengeUserResponse response = new ChallengeUserResponse();

        // 챌린지 정보 매핑
        response.challengeId = challenge.getId();
        response.title = challenge.getTitle();
        response.category = challenge.getCategory();
        response.startDate = challenge.getChallengePeriod().getStartDate();
        response.endDate = challenge.getChallengePeriod().getEndDate();
        response.uploadStartTime = challenge.getChallengePeriod().getUploadStartTime();
        response.uploadEndTime = challenge.getChallengePeriod().getUploadEndTime();
        response.participantsLimit = challenge.getParticipantsLimit();
        response.description = challenge.getDescription();
        response.authMethod = challenge.getAuthMethod();
        response.mainImage = challenge.getChallengeImages().getMainImage();
        response.successImage = challenge.getChallengeImages().getSuccessImage();
        response.failImage = challenge.getChallengeImages().getFailImage();
        response.uploadUserNickname = challenge.getUser().getNickname();
        response.participants = challenge.getParticipants();
        response.totalPrize = challenge.getPrize();
        response.state = challenge.getState();
        response.prize = userChallenge.getEarnedPrize();
        response.isClosed = challenge.isClosed();

        // 참가 유저 정보 매핑
        response.participantNickname = userChallenge.getUser().getNickname();
        response.participantName = userChallenge.getUser().getName();
        response.participantUuid = userChallenge.getUser().getId().toString();
        response.participantEmail = userChallenge.getUser().getEmail();

        // 진행률 매핑
        response.progress = userChallenge.getProgress();
        response.deposit = userChallenge.getDeposit();

        // 상금 정보 매핑
        return response;
    }
}
