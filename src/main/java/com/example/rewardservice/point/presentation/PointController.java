package com.example.rewardservice.point.presentation;

import com.example.rewardservice.auth.AuthUser;
import com.example.rewardservice.point.application.PointService;
import com.example.rewardservice.point.application.dto.GiftPointRequest;
import com.example.rewardservice.point.application.dto.ViewPointRequest;
import com.example.rewardservice.point.domain.Point;
import com.example.rewardservice.security.jwt.JwtTokenExtractor;
import com.example.rewardservice.user.application.UserService;
import com.example.rewardservice.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/points")
@RequiredArgsConstructor
public class PointController {
    private final PointService pointService;
    private final UserService userService;
    private final JwtTokenExtractor jwtTokenExtractor;

    @PostMapping("/gift")
    public ResponseEntity<Point> gift(@RequestBody GiftPointRequest giftPointRequest) {
        String sender = jwtTokenExtractor.getCurrentUserEmail();
        pointService.giftPoints(giftPointRequest);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestBody Map<String, String> request) {
        String email = request.get("recipientId");
        boolean exists = userService.emailExists(email);
        return ResponseEntity.ok(Map.of("email", exists));
    }





}
