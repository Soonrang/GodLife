package com.example.rewardservice.Event.application.dto;

import lombok.Getter;

@Getter
public class AttendanceDTO {

    private String eventName;
    private int participationCount;
    private long bonusPoints;

}
