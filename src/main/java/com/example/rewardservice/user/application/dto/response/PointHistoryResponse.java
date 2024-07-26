package com.example.rewardservice.user.application.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PointHistoryResponse {
    private String type;
    private long amount;
    private String description;
    private LocalDateTime date;

    public PointHistoryResponse(String type, long amount, String description, LocalDateTime date) {
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }
}