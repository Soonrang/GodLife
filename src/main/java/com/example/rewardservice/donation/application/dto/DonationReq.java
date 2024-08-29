package com.example.rewardservice.donation.application.dto;


import java.util.UUID;

public record DonationReq(UUID id, long points) {
}
