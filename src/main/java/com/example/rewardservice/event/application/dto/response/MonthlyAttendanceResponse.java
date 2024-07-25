package com.example.rewardservice.event.application.dto.response;

import com.example.rewardservice.event.domain.EventType.AttendanceEvent;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MonthlyAttendanceResponse {

    private final int attendanceCount;
    private final long totalPoints;
    private final boolean hasAttendance;
    private final EventDTO event;

    public MonthlyAttendanceResponse(int attendanceCount, long totalPoints, boolean hasAttendance, AttendanceEvent event) {
        this.attendanceCount = attendanceCount;
        this.totalPoints = totalPoints;
        this.hasAttendance = hasAttendance;
        this.event = new EventDTO(event);
    }

    @Getter
    public static class EventDTO {
        private final String name;
        private final String description;
        private final LocalDateTime startDate;
        private final LocalDateTime endDate;

        public EventDTO(AttendanceEvent event) {
            this.name = event.getName();
            this.description = event.getDescription();
            this.startDate = event.getStartDate();
            this.endDate = event.getEndDate();
        }
    }
}