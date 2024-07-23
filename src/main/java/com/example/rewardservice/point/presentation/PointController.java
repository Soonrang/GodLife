package com.example.rewardservice.point.presentation;

import com.example.rewardservice.auth.AuthUser;
import com.example.rewardservice.point.application.PointService;
import com.example.rewardservice.point.application.dto.GiftPointRequest;
import com.example.rewardservice.point.domain.Point;
import com.example.rewardservice.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/points")
@RequiredArgsConstructor
public class PointController {
    private final PointService pointService;

    @PostMapping("/gift")
    public ResponseEntity<Point> gift(@AuthUser User sender, @RequestBody GiftPointRequest giftPointRequest) {
        pointService.giftPoints(sender, giftPointRequest);
        return ResponseEntity.noContent().build();
    }
}
