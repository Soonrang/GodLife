package com.example.rewardservice.challenge.application.response;

import java.time.LocalDate;
import java.util.UUID;

public class ChallengeJoinResponse {

    private UUID challengeId;
    private String title;
    private String status;

    //변경날짜 통합
    private LocalDate changeDate;

    //reason 통합
    private String reason;



}
