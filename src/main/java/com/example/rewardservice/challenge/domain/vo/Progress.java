package com.example.rewardservice.challenge.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@EqualsAndHashCode
@Embeddable
public class Progress {

    private UUID userId;
    private UUID challengeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate currentDate;
    private int completedDays;
    private int totalDays;

    public Progress(UUID userId, UUID challengeId, LocalDate startDate, LocalDate endDate, LocalDate currentDate, int completedDays, int totalDays) {
        this.userId = userId;
        this.challengeId = challengeId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.currentDate = currentDate;
        this.completedDays = completedDays;
        this.totalDays = totalDays;
    }

    public Progress() {

    }

    public double getProgressPercentage() {
        return ((double) completedDays/totalDays) * 100;
    }

    public Progress updateProgress(int newCompletedDays) {
        return new Progress(userId, challengeId, startDate, endDate, currentDate, newCompletedDays, totalDays);
    }
}
