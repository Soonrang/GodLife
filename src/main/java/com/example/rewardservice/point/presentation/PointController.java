package com.example.rewardservice.point.presentation;

import com.example.rewardservice.point.application.GiftRequest;
import com.example.rewardservice.security.jwt.JwtTokenExtractor;
import com.example.rewardservice.user.application.request.GiftPointRequest;
import com.example.rewardservice.user.application.service.GiftPointService;
import com.example.rewardservice.user.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/points")
@RequiredArgsConstructor
public class PointController {

    private final JwtTokenExtractor jwtTokenExtractor;
    private final GiftPointService giftPointService;
    private final UserService userService;

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

}
