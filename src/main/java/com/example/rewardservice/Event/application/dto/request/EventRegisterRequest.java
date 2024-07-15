package com.example.rewardservice.Event.application.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventRegisterRequest {

    private String name;

    private String description;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String eventState;

}
