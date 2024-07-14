package com.example.rewardservice.Event.application.dto;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class EventResponse {
    private String id;
    private String name;
    private String description;
    private String eventType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
