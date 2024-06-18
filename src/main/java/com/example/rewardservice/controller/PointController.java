package com.example.rewardservice.controller;

import com.example.rewardservice.domain.Point;
import com.example.rewardservice.domain.PointLog;
import com.example.rewardservice.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/points")
@RequiredArgsConstructor
public class PointController {

    private final PointService pointService;

    @PostMapping("/create")
    public ResponseEntity<Point> createPoint(@RequestParam String userId, @RequestParam long initialPoints) {
        Point point = pointService.createPoint(userId, initialPoints);
        return ResponseEntity.ok(point);
    }

    @PostMapping("/{pointId}/earn")
    public ResponseEntity<PointLog> earnPoints(@PathVariable UUID pointId, @RequestParam long points, @RequestParam String reason) {
        PointLog pointLog = pointService.earnPoints(pointId, points, reason);
        return ResponseEntity.ok(pointLog);
    }

    @PostMapping("/{pointId}/use")
    public ResponseEntity<PointLog> usePoints(@PathVariable UUID pointId, @RequestParam long points, @RequestParam String reason) {
        PointLog pointLog = pointService.usePoints(pointId, points, reason);
        return ResponseEntity.ok(pointLog);
    }

    @GetMapping("/{pointId}")
    public ResponseEntity<Point> getPointById(@PathVariable UUID pointId) {
        Point point = pointService.getPointById(pointId);
        return ResponseEntity.ok(point);
    }
}