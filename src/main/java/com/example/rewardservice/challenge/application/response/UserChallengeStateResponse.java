package com.example.rewardservice.challenge.application.response;

import com.example.rewardservice.challenge.domain.Challenge;
import com.example.rewardservice.challenge.domain.ChallengeHistory;
import com.example.rewardservice.challenge.domain.ChallengePost;
import com.example.rewardservice.challenge.domain.UserChallenge;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Getter
public class UserChallengeStateResponse {

    //챌린지 정보
    private UUID challengeId;
    private String title;
    private String category;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime uploadStartTime;
    private  LocalTime uploadEndTime;
    private long participantsLimit;
    private String description;
    private String authMethod;
    private String mainImage;
    private String successImage;
    private String failImage;
    private String uploadUserNickname;
    private long participants;
    private Boolean isJoined;
    private String state; //진행중/종료/모집마감 등
    private long prize;

    private double progress;

    //참가유저정보
    private String participantNickname;
    private String participantName;
    private String participantUuid;
    private String participantEmail;

    //참가유저인증정보
    private List<ChallengePostResponse> checkRecords;

    public static UserChallengeStateResponse from(Challenge challenge, UserChallenge userChallenge, List<ChallengePost> challengePosts) {
        // Create and return the response object
        UserChallengeStateResponse response = new UserChallengeStateResponse();

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
        response.state = challenge.getState();
        response.prize = challenge.getPrize();

        // 유저 정보 매핑
        response.participantNickname = userChallenge.getUser().getNickname();
        response.participantName = userChallenge.getUser().getName();
        response.participantUuid = userChallenge.getUser().getId().toString();
        response.participantEmail = userChallenge.getUser().getEmail();

        // 진행률 매핑
        response.progress = userChallenge.getProgress();

        // 인증 정보 매핑
        response.checkRecords = challengePosts.stream()
                .map(ChallengePostResponse::from)
                .toList(); // Stream을 사용하여 인증 정보 리스트 생성

        return response;
    }
}

