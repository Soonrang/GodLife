package com.example.rewardservice.point.presentation;

import com.example.rewardservice.point.application.GiftRequest;
import com.example.rewardservice.point.application.PointRecordResponse;
import com.example.rewardservice.point.application.PointService;
import com.example.rewardservice.security.jwt.JwtTokenExtractor;
import com.example.rewardservice.user.application.request.GiftPointRequest;
import com.example.rewardservice.user.application.service.GiftPointService;
import com.example.rewardservice.user.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/points")
@RequiredArgsConstructor
public class PointController {

    private final JwtTokenExtractor jwtTokenExtractor;
    private final GiftPointService giftPointService;
    private final PointService pointService;

    @PostMapping("/gift")
    public ResponseEntity<Void> gift(@RequestBody GiftRequest giftRequest) {
        String sender = jwtTokenExtractor.getCurrentUserEmail();
        giftPointService.giftPoints(sender, giftRequest);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
        boolean exists = giftPointService.giftUserCheck(email);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/gift-record")
    public ResponseEntity<List<PointRecordResponse>> viewGiftRecord() {
        String email = jwtTokenExtractor.getCurrentUserEmail();
        List<PointRecordResponse> giftList = pointService.getGiftHistory(email);
        return ResponseEntity.ok(giftList);
    }

    @GetMapping("/donation-record")
    public ResponseEntity<List<PointRecordResponse>> viewDonationRecord() {
        String email = jwtTokenExtractor.getCurrentUserEmail();
        List<PointRecordResponse> donationList = pointService.getDonationHistory(email);
        return ResponseEntity.ok(donationList);
    }

    @GetMapping("/event-record")
    public ResponseEntity<List<PointRecordResponse>> viewEventRecord() {
        String email = jwtTokenExtractor.getCurrentUserEmail();
        List<PointRecordResponse> participationList = pointService.getParticipationHistory(email);
        return ResponseEntity.ok(participationList);
    }
}
