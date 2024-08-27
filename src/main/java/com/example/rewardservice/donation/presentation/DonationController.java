package com.example.rewardservice.donation.presentation;

import com.example.rewardservice.admin.application.dto.response.DonationResponse;
import com.example.rewardservice.donation.application.DonationService;
import com.example.rewardservice.donation.application.dto.DonatePointRequest;
import com.example.rewardservice.donation.application.dto.DonationReq;
import com.example.rewardservice.donation.application.dto.UserDonationResponse;
import com.example.rewardservice.security.jwt.JwtTokenExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class DonationController {

    private final DonationService donationService;
    private final JwtTokenExtractor jwtTokenExtractor;
    @PostMapping("/api/donation")
    public ResponseEntity<UserDonationResponse> donationPoints(@RequestBody DonationReq donationReq) {
        String email = jwtTokenExtractor.getCurrentUserEmail();
        UserDonationResponse response = donationService.donatePoints(donationReq.id(), email, donationReq.points());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/donation/view-all")
    public ResponseEntity<List<DonationResponse>> getAllDonations() {
        return ResponseEntity.ok(donationService.getAllDonations());
    }


    @GetMapping("/api/donation/view/{id}")
    public ResponseEntity<DonationResponse> getDonationById(@PathVariable UUID id) {
        DonationResponse donationInfo= donationService.getDonationInfo(id);
        return ResponseEntity.ok(donationInfo);
    }
}
//        UUID donationId, String email, long points
