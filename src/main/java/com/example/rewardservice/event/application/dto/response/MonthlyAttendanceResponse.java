package com.example.rewardservice.event.application.dto.response;

import com.example.rewardservice.event.domain.Events.AttendanceEvent;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MonthlyAttendanceResponse {

    private final int attendanceCount;
    private final long totalPoints;
    private final boolean hasAttendance;

    public MonthlyAttendanceResponse(int attendanceCount, long totalPoints, boolean hasAttendance) {
        this.attendanceCount = attendanceCount;
        this.totalPoints = totalPoints;
        this.hasAttendance = hasAttendance;
    }

}