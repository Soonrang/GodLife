package com.example.rewardservice.challenge.domain;

import com.example.rewardservice.common.BaseEntity;
import com.example.rewardservice.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class UserChallenge extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    // 취소한 경우 false
    private boolean isJoined;

    //참가중, 성공, 실패
    private String status;

    private double progress;

    //챌린지 인증 횟수(인증됨 기준)
    private long authCount;

    // 유저가 건 상금
    private long deposit;

    // 성공시 획득 포인트
    private long earnedPrize;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    private Challenge challenge;

    @OneToMany(mappedBy = "userChallenge", cascade = CascadeType.ALL)
    private List<ChallengePost> challengePosts;

    @ManyToOne
    private UserChallenge userChallenge;

    public UserChallenge(User user, Challenge challenge, String status, boolean isJoined, long points) {
        this.user = user;
        this.challenge = challenge;
        this.status = status;
        this.progress = 0.0;
        this.isJoined = isJoined;
        this.deposit  = points;
    }

    public void calculateProgress() {
        long totalDays = ChronoUnit.DAYS.between(challenge.getChallengePeriod().getStartDate(), challenge.getChallengePeriod().getEndDate());
        totalDays = Math.max(totalDays, 1);
        this.progress = Math.round((authCount / (double) totalDays * 100) * 10.0) / 10.0;
    }

    public void changeStatusToFalse() {
        this.isJoined = false;
    }

    public void plusAuthCount() {
        this.authCount++;
    }

    public void minusAuthCount() {
        this.authCount--;
    }

    public void challengeStatusToFail(String failStatus) {
        this.status = failStatus;
    }

    public void challengeStatusToSuccess(String successStatus) {
        this.status = successStatus;
    }

    public void updatePrize(long points) {
        this.earnedPrize = points;
    }

}
