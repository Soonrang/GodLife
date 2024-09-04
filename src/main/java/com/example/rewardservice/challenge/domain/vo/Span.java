package com.example.rewardservice.challenge.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Span {

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;


    public boolean isAllDay() {
        return isStartOfTheDate() && isEndOfTheDate();
    }

    private boolean isStartOfTheDate() {
        return startDate.getHour() == 0 && startDate.getMinute() == 0;
    }

    private boolean isEndOfTheDate() {
        return endDate.getHour() == 23 && endDate.getMinute() == 59;
    }
}
