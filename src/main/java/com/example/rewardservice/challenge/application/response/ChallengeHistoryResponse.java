package com.example.rewardservice.challenge.application.response;

import com.example.rewardservice.challenge.domain.ChallengeHistory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ChallengeHistoryResponse (
        UUID challengeId,

        //챌린지 이름
        String title,

        // 유형 (참가,취소,상금획득)
        String status,

        // 발생 날짜
        LocalDateTime changeDate,

        // 사유 (현재 단계에서는 필요x)
        String reason,

        // 변동 포인트
        long point
){

    private static ChallengeHistoryResponse from(ChallengeHistory challengeHistory,String status) {
        return new ChallengeHistoryResponse(
               challengeHistory.getChallenge().getId(),
                challengeHistory.getChallenge().getTitle(),
                status,
                challengeHistory.getCreateAt(),
                challengeHistory.getReason(),
                1000
        );
    }
}
