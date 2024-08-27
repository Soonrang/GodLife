package com.example.rewardservice.donation.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserDonationResponse(
        UUID donationId,
        String donationTitle,
        String userName,
        long donatedPoints,
        long userCurrentPoints,
        LocalDateTime donationDate
) {
}