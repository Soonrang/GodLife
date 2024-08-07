package com.example.rewardservice.admin.presentation;

import com.example.rewardservice.admin.application.AdminDonationService;
import com.example.rewardservice.admin.application.dto.AdminDonationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/donation")
@RequiredArgsConstructor
public class AdminDonationController {
    private final AdminDonationService adminDonationService;

    @PostMapping("/register")
    public ResponseEntity<Void> registerDonation(@ModelAttribute AdminDonationRequest adminDonationRequest) {
        adminDonationService.registerDonation(adminDonationRequest);
        return ResponseEntity.ok().build();
    }


}