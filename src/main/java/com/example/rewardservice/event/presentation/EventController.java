package com.example.rewardservice.event.presentation;

import com.example.rewardservice.event.application.service.AttendanceService;
import com.example.rewardservice.event.application.service.RouletteService;
import com.example.rewardservice.event.application.response.MonthlyAttendanceResponse;
import com.example.rewardservice.event.application.service.ViewCountService;
import com.example.rewardservice.event.application.request.ViewPointRequest;
import com.example.rewardservice.security.jwt.JwtTokenExtractor;
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
    private final ViewCountService viewCountService;

    //출석
    @PostMapping("/participate/{eventId}")
    public ResponseEntity<Void> participateInAttendanceEvent(@PathVariable UUID eventId) {
        String email = jwtTokenExtractor.getCurrentUserEmail();
        attendanceService.participateInAttendanceEvent(eventId, email);
        return ResponseEntity.ok().build();
    }

//    @GetMapping("/event/check/{eventId}")
//    public ResponseEntity<Boolean> checkTodayAttendance(@PathVariable UUID eventId) {
//        String email = jwtTokenExtractor.getCurrentUserEmail();
//        boolean hasAttended = attendanceService.checkTodayAttendance(eventId, email);
//        return ResponseEntity.ok(hasAttended);
//    }

    @GetMapping("/monthlyCount/{eventId}")
    public ResponseEntity<MonthlyAttendanceResponse> getMonthlyAttendance(@PathVariable UUID eventId) {
        String userEmail = jwtTokenExtractor.getCurrentUserEmail();
        MonthlyAttendanceResponse response = attendanceService.getAttendanceData(userEmail, eventId);
        return ResponseEntity.ok(response);

    }


    //-----------------------------------룰렛------------------------------------
    @GetMapping("/roulette/count/{eventId}")
    public ResponseEntity<Integer> hasParticipatedToday(@PathVariable UUID eventId) {
        String email = jwtTokenExtractor.getCurrentUserEmail();
        int restRouletteCount = rouletteService.getRouletteCount(email, eventId);
        return ResponseEntity.ok(restRouletteCount);
    }

    @PostMapping("/roulette/updatePoint/{eventId}")
    public ResponseEntity<String> updateRoulettePoint(@PathVariable UUID eventId, @RequestBody long earnedPoints) {
        try {
            String email = jwtTokenExtractor.getCurrentUserEmail();
            rouletteService.participateInRouletteEvent(email, eventId, earnedPoints);
            return ResponseEntity.ok("포인트가 성공적으로 업데이트되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/view-point/{eventId}")
    public ResponseEntity<Void> viewPoint(@PathVariable UUID eventId, @RequestBody ViewPointRequest viewPointRequest) {
        String user = jwtTokenExtractor.getCurrentUserEmail();
        viewCountService.viewPoints(eventId,user, viewPointRequest.getPoints());
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/checkParticipation")
    public ResponseEntity<Boolean> checkParticipation(@RequestParam UUID eventId) {
        String userEmail = jwtTokenExtractor.getCurrentUserEmail();
        boolean hasParticipated = viewCountService.checkIfUserParticipatedToday(eventId,userEmail);
        return ResponseEntity.ok(hasParticipated);
    }

}
