package com.example.rewardservice.admin.application.dto;

import com.example.rewardservice.event.domain.EventPeriod;
import com.example.rewardservice.event.domain.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class EventResponse {
    private UUID id;
    private String name;
    private String eventState;
    private EventType eventType;
    private EventPeriod eventPeriod;
}
