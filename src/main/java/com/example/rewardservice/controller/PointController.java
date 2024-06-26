package com.example.rewardservice.controller;

import com.example.rewardservice.dto.User.UserDTO;
import com.example.rewardservice.dto.Point.PointLogDTO;
import com.example.rewardservice.service.PointService;
import com.example.rewardservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/points")
@RequiredArgsConstructor
public class PointController {
    private final PointService pointService;

    @PostMapping("/{pointId}/earn")
    public ResponseEntity<PointLogDTO> earnPoints(@PathVariable UUID pointId, @RequestParam long points, @RequestParam String reason) {
        PointLogDTO pointLog = pointService.earnPoints(pointId, points, reason);
        return ResponseEntity.ok(pointLog);
    }

    @PostMapping("/{pointId}/use")
    public ResponseEntity<PointLogDTO> usePoints(@PathVariable UUID pointId, @RequestParam long points, @RequestParam String reason) {
        PointLogDTO pointLog = pointService.usePoints(pointId, points, reason);
        return ResponseEntity.ok(pointLog);
    }

}