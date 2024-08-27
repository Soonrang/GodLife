package com.example.rewardservice.admin.presentation;

import com.example.rewardservice.admin.application.AdminDonationService;
import com.example.rewardservice.admin.application.dto.request.DonationRegisterRequest;
import com.example.rewardservice.admin.application.dto.response.DonationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/donation")
@RequiredArgsConstructor
public class AdminDonationController {
    private final AdminDonationService adminDonationService;

    @PostMapping("/register")
    public ResponseEntity<DonationResponse> registerDonation(@ModelAttribute DonationRegisterRequest donationRegisterRequest) {
        DonationResponse donationResponse = adminDonationService.registerDonation(donationRegisterRequest);
        return ResponseEntity.ok(donationResponse);
    }






}