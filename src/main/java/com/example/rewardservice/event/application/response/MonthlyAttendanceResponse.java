package com.example.rewardservice.event.application.response;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class MonthlyAttendanceResponse {

    private final int attendanceCount;
    private final long totalPoints;
    private final boolean hasAttendance;
    private final List<LocalDateTime> attendanceList;

    public MonthlyAttendanceResponse(int attendanceCount, long totalPoints, boolean hasAttendance, List<LocalDateTime> attendanceList) {
        this.attendanceCount = attendanceCount;
        this.totalPoints = totalPoints;
        this.hasAttendance = hasAttendance;
        this.attendanceList = attendanceList;
    }

}