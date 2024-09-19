package com.example.rewardservice.challenge.presentation;

import com.example.rewardservice.challenge.application.request.ChallengeJoinRequest;
import com.example.rewardservice.challenge.application.response.ChallengeHistoryResponse;
import com.example.rewardservice.challenge.application.response.ChallengeInfoResponse;
import com.example.rewardservice.challenge.application.service.ChallengeHistoryService;
import com.example.rewardservice.challenge.application.service.ChallengeJoinService;
import com.example.rewardservice.security.jwt.JwtTokenExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ChallengeJoinController {

    private final ChallengeJoinService challengeJoinService;
    private final JwtTokenExtractor jwtTokenExtractor;
    private final ChallengeHistoryService challengeHistoryService;

    @PostMapping("/api/challenge/join")
    public ResponseEntity<UUID> joinChallenge(@RequestBody ChallengeJoinRequest challengeJoinRequest) {
        String email = jwtTokenExtractor.getCurrentUserEmail();
        UUID participationId = challengeJoinService.joinChallenge(challengeJoinRequest,email);
        return ResponseEntity.ok(participationId);
    }

    // 유저가 챌린지 참가를 취소하는 요청 처리
    @PostMapping("/leave")
    public ResponseEntity<UUID> cancelChallenge(@RequestParam UUID challengeId) {
        String email = jwtTokenExtractor.getCurrentUserEmail();
        UUID participationId = challengeJoinService.cancelChallenge(challengeId, email);
        return ResponseEntity.ok(participationId);
    }

    // 유저가 참가한 챌린지 목록 조회 요청 : 진행중
    @GetMapping("/api/user/challenge/participating")
    public ResponseEntity<Page<ChallengeInfoResponse>> getJoinedChallenges(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam String state
    ) {
        String email = jwtTokenExtractor.getCurrentUserEmail();
        Page<ChallengeInfoResponse> joinedChallenges = challengeJoinService.getJoinedChallenges(email,page,size,state);
        return ResponseEntity.ok(joinedChallenges);
    }


    // 챌린지 신청 및 취소 이력 조회
    @GetMapping("/api/challenge/applied")
    public ResponseEntity<Page<ChallengeHistoryResponse>> getChallengeHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        String email = jwtTokenExtractor.getCurrentUserEmail();
        Page<ChallengeHistoryResponse> historyResponses = challengeHistoryService.getChallengeHistory(email, page, size);
        return ResponseEntity.ok(historyResponses);
    }




}
