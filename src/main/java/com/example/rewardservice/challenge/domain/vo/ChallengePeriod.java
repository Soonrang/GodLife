package com.example.rewardservice.challenge.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Embeddable
@Getter
public class ChallengePeriod {

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "start_upload_time")
    private LocalTime uploadStartTime;

    @Column(name = "end_upload_time")
    private LocalTime uploadEndTime;

    public ChallengePeriod() {
    }

    public ChallengePeriod(LocalDate startDate, LocalDate endDate, LocalTime uploadStartTime, LocalTime uploadEndTime) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.uploadStartTime = uploadStartTime;
        this.uploadEndTime = uploadEndTime;
    }

}
