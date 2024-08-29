package com.example.rewardservice.event.presentation;

import com.example.rewardservice.event.application.request.UpdatePointRequest;
import com.example.rewardservice.event.application.request.WeatherPointRequest;
import com.example.rewardservice.event.application.service.AttendanceService;
import com.example.rewardservice.event.application.service.RouletteService;
import com.example.rewardservice.event.application.response.MonthlyAttendanceResponse;
import com.example.rewardservice.event.application.service.ViewCountService;
import com.example.rewardservice.event.application.request.ViewPointRequest;
import com.example.rewardservice.event.application.service.WeatherService;
import com.example.rewardservice.security.jwt.JwtTokenExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/event")
@RequiredArgsConstructor
public class EventController {

    private final RouletteService rouletteService;
    private final AttendanceService attendanceService;
    private final JwtTokenExtractor jwtTokenExtractor;
    private final ViewCountService viewCountService;
    private final WeatherService weatherService;

    //출석
    @PostMapping("/participate/{eventId}")
    public ResponseEntity<Void> participateInAttendanceEvent(@PathVariable UUID eventId) {
        String email = jwtTokenExtractor.getCurrentUserEmail();
        attendanceService.participateInAttendanceEvent(eventId, email);
        return ResponseEntity.ok().build();
    }

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
    public ResponseEntity<String> updateRoulettePoint(@PathVariable UUID eventId, @RequestBody UpdatePointRequest updatePointRequest) {
        String email = jwtTokenExtractor.getCurrentUserEmail();
        rouletteService.participateInRouletteEvent(email, eventId, updatePointRequest.getEarnedPoints());
        return ResponseEntity.ok("포인트가 성공적으로 업데이트되었습니다.");

    }
    @PostMapping("/view-point/{eventId}")
    public ResponseEntity<Void> viewPoint(@PathVariable UUID eventId, @RequestBody UpdatePointRequest updatePointRequest) {
        String user = jwtTokenExtractor.getCurrentUserEmail();
        viewCountService.viewPoints(eventId,user, updatePointRequest.getEarnedPoints());
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/checkParticipation")
    public ResponseEntity<Boolean> checkParticipation(@RequestParam UUID eventId) {
        String userEmail = jwtTokenExtractor.getCurrentUserEmail();
        boolean hasParticipated = viewCountService.checkIfUserParticipatedToday(eventId,userEmail);
        return ResponseEntity.ok(hasParticipated);
    }

    @PostMapping("/weather-event")
    public ResponseEntity<String> weatherPoint(@RequestParam UUID eventId, @RequestBody WeatherPointRequest request) {
        String user = jwtTokenExtractor.getCurrentUserEmail();
        weatherService.getWeatherPoints(eventId,user,request);
        return ResponseEntity.ok("포인트가 성공적으로 업데이트되었습니다.");
    }

}
