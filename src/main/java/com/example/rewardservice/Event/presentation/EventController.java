package com.example.rewardservice.Event.presentation;

import com.example.rewardservice.Event.application.AttendanceService;
import com.example.rewardservice.Event.application.RouletteService;
import com.example.rewardservice.Event.application.dto.response.MonthlyAttendanceResponse;
import com.example.rewardservice.auth.JwtTokenExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {

    private final RouletteService rouletteService;
    private final AttendanceService attendanceService;
    private final JwtTokenExtractor jwtTokenExtractor;

    //출석
    @PostMapping("/participate/{eventId}")
    public ResponseEntity<String> participateInAttendanceEvent(@PathVariable UUID eventId) {
        String email = jwtTokenExtractor.getCurrentUserEmail();
        try {
            attendanceService.participateInAttendanceEvent(eventId, email);
            return ResponseEntity.ok("출석이 완료되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/check/{eventId}")
    public ResponseEntity<Boolean> checkTodayAttendance(@PathVariable UUID eventId) {
        String email = jwtTokenExtractor.getCurrentUserEmail();
        boolean hasAttended = attendanceService.checkTodayAttendance(eventId, email);
        return ResponseEntity.ok(hasAttended);
    }

    @GetMapping("/monthlyCount/{eventId}")
    public ResponseEntity<MonthlyAttendanceResponse> getMonthlyAttendanceCount(@PathVariable UUID eventId) {
        String email = jwtTokenExtractor.getCurrentUserEmail();
        MonthlyAttendanceResponse response = attendanceService.getAttendanceData(email, eventId);
        return ResponseEntity.ok(response);
    }
}
