package com.example.rewardservice.challenge.presentation;

import com.example.rewardservice.challenge.application.response.ChallengeInfoResponse;
import com.example.rewardservice.challenge.application.response.ChallengeJoinResponse;
import com.example.rewardservice.challenge.application.service.ChallengeJoinService;
import com.example.rewardservice.security.jwt.JwtTokenExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ChallengeJoinController {

    private final ChallengeJoinService challengeJoinService;
    private final JwtTokenExtractor jwtTokenExtractor;

    @PostMapping("/api/challenge/join")
    public ResponseEntity<UUID> joinChallenge(@RequestBody Map<String, Object> requestBody) {
        String email = jwtTokenExtractor.getCurrentUserEmail();
        UUID challengeId = UUID.fromString((String) requestBody.get("challengeId"));
        UUID participationId = challengeJoinService.joinChallenge(challengeId, email);
        return ResponseEntity.ok(participationId);
    }

    // 유저가 챌린지 참가를 취소하는 요청 처리
    @PostMapping("/leave")
    public ResponseEntity<UUID> cancelChallenge(@RequestParam UUID challengeId) {
        String email = jwtTokenExtractor.getCurrentUserEmail();
        UUID participationId = challengeJoinService.cancelChallenge(challengeId, email);
        return ResponseEntity.ok(participationId);
    }

    // 유저가 참가한 챌린지 목록 조회
    @GetMapping("/api/challenge/applied")
    public ResponseEntity<List<ChallengeInfoResponse>> getJoinedChallenges() {
        String email = jwtTokenExtractor.getCurrentUserEmail();
        List<ChallengeInfoResponse> joinedChallenges = challengeJoinService.getJoinedChallenges(email);
        return ResponseEntity.ok(joinedChallenges);
    }



}
