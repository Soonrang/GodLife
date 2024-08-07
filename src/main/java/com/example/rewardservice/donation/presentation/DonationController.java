package com.example.rewardservice.donation.presentation;

import com.example.rewardservice.donation.application.DonationService;
import com.example.rewardservice.donation.application.dto.DonatePointRequest;
import com.example.rewardservice.security.jwt.JwtTokenExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class DonationController {

    private final DonationService donationService;
    private final JwtTokenExtractor jwtTokenExtractor;
    @PostMapping("/api/donation")
    public ResponseEntity<Void> donationPoints(@RequestBody DonatePointRequest donatePointRequest) {
        String email = jwtTokenExtractor.getCurrentUserEmail();
        donationService.donatePoints(donatePointRequest,email);
        return ResponseEntity.ok().build();
    }
}
