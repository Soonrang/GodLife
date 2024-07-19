package com.example.rewardservice.event.presentation;

import com.example.rewardservice.event.application.AttendanceService;
import com.example.rewardservice.event.application.RouletteService;
import com.example.rewardservice.event.application.dto.request.EventRegisterRequest;
import com.example.rewardservice.event.application.dto.response.MonthlyAttendanceResponse;
import com.example.rewardservice.security.jwt.JwtTokenExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
//@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {

    private final RouletteService rouletteService;
    private final AttendanceService attendanceService;
    private final JwtTokenExtractor jwtTokenExtractor;

    // 출석 이벤트 등록
    @PostMapping("/guest/register")
    public ResponseEntity<String> registerEvent(@RequestBody EventRegisterRequest eventRegisterRequest) {
        try {
            attendanceService.registerEvent(eventRegisterRequest);
            return ResponseEntity.ok("이벤트가 성공적으로 등록되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //출석
    @PostMapping("/event/participate/{eventId}")
    public ResponseEntity<Void> participateInAttendanceEvent(@PathVariable UUID eventId) {
        String email = jwtTokenExtractor.getCurrentUserEmail();
        attendanceService.participateInAttendanceEvent(eventId, email);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/event/check/{eventId}")
    public ResponseEntity<Boolean> checkTodayAttendance(@PathVariable UUID eventId) {
        String email = jwtTokenExtractor.getCurrentUserEmail();
        boolean hasAttended = attendanceService.checkTodayAttendance(eventId, email);
        return ResponseEntity.ok(hasAttended);
    }

    @GetMapping("/event/monthlyCount/{eventId}")
    public ResponseEntity<MonthlyAttendanceResponse> getMonthlyAttendance(@PathVariable UUID eventId) {
        String userEmail = jwtTokenExtractor.getCurrentUserEmail();
        MonthlyAttendanceResponse response = attendanceService.getAttendanceData(userEmail, eventId);
        return ResponseEntity.ok(response);

    }


    //-----------------------------------룰렛------------------------------------
    @GetMapping("/event/roulette/count/{eventId}")
    public ResponseEntity<Integer> hasParticipatedToday(@PathVariable UUID eventId) {
        String email = jwtTokenExtractor.getCurrentUserEmail();
        int restRouletteCount = rouletteService.getRouletteCount(email, eventId);
        return ResponseEntity.ok(restRouletteCount);
    }

    @PostMapping("/event/roulette/updatePoint/{eventId}/{earnedPoints}")
    public ResponseEntity<String> updateRoulettePoint(@PathVariable UUID eventId, @PathVariable long earnedPoints) {
        try {
            String email = jwtTokenExtractor.getCurrentUserEmail();
            rouletteService.participateInRouletteEvent(email, eventId, earnedPoints);
            return ResponseEntity.ok("포인트가 성공적으로 업데이트되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
