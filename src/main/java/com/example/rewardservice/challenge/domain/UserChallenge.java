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

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    private Challenge challenge;

    private boolean isJoined;

    //참가중, 성공, 실패
    private String status;

    private double progress;

    public void changeStatus(String status) {
        this.status = status;
    }

    public UserChallenge(User user, Challenge challenge, String status, boolean isJoined) {
        this.user = user;
        this.challenge = challenge;
        this.status = status;
        this.progress = 0.0;
        this.isJoined = isJoined;
    }

    public void calculateProgress(Challenge challenge) {
        long totalDays = ChronoUnit.DAYS.between(challenge.getChallengePeriod().getStartDate(), challenge.getChallengePeriod().getEndDate());

        long elapsedDays = ChronoUnit.DAYS.between(challenge.getChallengePeriod().getStartDate(), LocalDate.now());

        if (elapsedDays >= totalDays) {
            this.progress = 100.0;
        } else {
            this.progress = ((double) elapsedDays / totalDays) * 100;
        }
    }

    public void leaveChallenge() {
        this.isJoined = false;
    }

}
