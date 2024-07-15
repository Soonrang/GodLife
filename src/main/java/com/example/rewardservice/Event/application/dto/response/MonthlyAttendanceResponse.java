package com.example.rewardservice.Event.application.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MonthlyAttendanceResponse {

    private int monthlyAttendanceCount;
    private long accumulatedPoints;
    private String lastAttendanceTime;

    public MonthlyAttendanceResponse(int monthlyAttendanceCount, long accumulatedPoints, String lastAttendanceTime) {
        this.monthlyAttendanceCount = monthlyAttendanceCount;
        this.accumulatedPoints = accumulatedPoints;
        this.lastAttendanceTime = lastAttendanceTime;
    }


}
