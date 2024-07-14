package com.example.rewardservice.Event.presentation;

import com.example.rewardservice.Event.application.AttendanceService;
import com.example.rewardservice.auth.JwtTokenExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/event/attendance")
public class AttendanceController {
    private final AttendanceService attendanceService;
    private final JwtTokenExtractor jwtTokenExtractor;

//    @PostMapping("/participate")
//    public ResponseEntity<Void> participateInAttendanceEvent(@RequestParam UUID eventId) {
//        String email = jwtTokenExtractor.getCurrentUserEmail();
//        attendanceService.participateInAttendanceEvent(eventId,email);
//        return ResponseEntity.ok().build();
//    }
}
