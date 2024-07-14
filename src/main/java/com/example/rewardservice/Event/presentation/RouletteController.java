package com.example.rewardservice.Event.presentation;

import com.example.rewardservice.Event.application.AttendanceService;
import com.example.rewardservice.Event.application.RouletteService;
import com.example.rewardservice.Event.application.dto.UpdatePointRequest;
import com.example.rewardservice.auth.JwtTokenExtractor;
import com.example.rewardservice.user.application.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/event/roulette")
@RequiredArgsConstructor
public class RouletteController {

    private final AttendanceService attendanceService;
    private final RouletteService rouletteService;
    private final UserService userService;
    private final JwtTokenExtractor jwtTokenExtractor;

    @GetMapping("/hasParticipatedToday")
    public ResponseEntity<Boolean> hasParticipatedToday(@RequestParam String userId) {
        boolean hasParticipated = rouletteService.hasParticipatedToday(userId);
        return ResponseEntity.ok(hasParticipated);
    }

    @PostMapping("/updatePoint")
    public ResponseEntity<Void> updatePoint(@RequestBody UpdatePointRequest request) {
        String email = jwtTokenExtractor.getCurrentUserEmail();
        rouletteService.updatePoints(email, request.getEarnedPoints());
        return ResponseEntity.ok().build();
    }
}
