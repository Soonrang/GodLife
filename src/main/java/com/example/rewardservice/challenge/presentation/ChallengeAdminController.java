package com.example.rewardservice.challenge.presentation;

import com.example.rewardservice.challenge.application.response.ParticipantResponse;
import com.example.rewardservice.challenge.application.service.ChallengeAdminService;
import com.example.rewardservice.security.jwt.JwtTokenExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ChallengeAdminController {

    private final ChallengeAdminService challengeAdminService;
    private final JwtTokenExtractor jwtTokenExtractor;


    @GetMapping("/api/challenge/{challengeId}/participants")
    public ResponseEntity<List<ParticipantResponse>> getParticipants(@PathVariable UUID challengeId) {
        List<ParticipantResponse> participants = challengeAdminService.getParticipants(challengeId);
        return ResponseEntity.ok(participants);
    };

    @PutMapping("/api/challenge/{challengeId}/check-status/{postId}")
    public ResponseEntity<Void> updateChallengePostStatus(@PathVariable UUID challengeId, @PathVariable UUID postId, @RequestParam String status) {
        challengeAdminService.updateChallengePostStatus(challengeId,postId,status);
        return ResponseEntity.ok().build();
    }

}
