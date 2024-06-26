package com.example.rewardservice.controller.Event;

import com.example.rewardservice.dto.Event.AttendanceDTO;
import com.example.rewardservice.service.Event.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/events/attendance")
@RequiredArgsConstructor
public class AttendanceController {
    private final AttendanceService attendanceService;

    @PostMapping("/check-in")
    public ResponseEntity<AttendanceDTO> checkIn(@RequestParam String userId, @RequestParam String eventId) {
        AttendanceDTO attendanceDTO = attendanceService.checkIn(userId,eventId);
        return ResponseEntity.ok(attendanceDTO);
    }

}
