package com.example.rewardservice.donation.application.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class DonationRequest {
    private UUID id;
    private String title;
    private String email;
    private long points;
}
