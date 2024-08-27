package com.example.rewardservice.admin.application.dto.response;

import com.example.rewardservice.event.domain.EventPeriod;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class DonationResponse {
    private UUID id;
    private String title;
    private long currentAmount;
    private long targetAmount;
    private EventPeriod eventPeriod;
    private String donationImages;
}
