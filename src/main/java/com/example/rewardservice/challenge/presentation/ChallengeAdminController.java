package com.example.rewardservice.challenge.presentation;

import com.example.rewardservice.challenge.application.response.ChallengeAdminPostResponse;
import com.example.rewardservice.challenge.application.response.ChallengeInfoResponse;
import com.example.rewardservice.challenge.application.response.ParticipantResponse;
import com.example.rewardservice.challenge.application.service.ChallengeAdminService;
import com.example.rewardservice.security.jwt.JwtTokenExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ChallengeAdminController {

    private final ChallengeAdminService challengeAdminService;
    private final JwtTokenExtractor jwtTokenExtractor;

    @PostMapping("/api/user/challenge/admin/{challengeId}/close")
    public ResponseEntity<String> closeChallenge(@PathVariable UUID challengeId) {
        String email = jwtTokenExtractor.getCurrentUserEmail();
        challengeAdminService.closeChallenge(email, challengeId);
        return ResponseEntity.ok("챌린지가 종료되었습니다.");
    }


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
    @GetMapping("/api/user/challenge/admin")
    public ResponseEntity<Page<ChallengeInfoResponse>> getAdminChallenges(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String state
    ) {
        String email = jwtTokenExtractor.getCurrentUserEmail();
        Page<ChallengeInfoResponse> joinedChallenges = challengeAdminService.getAdminChallengesByUserEmail(email,page,size,state);
        return ResponseEntity.ok(joinedChallenges);
    }

    @GetMapping("/api/challenge/{challengeId}/check-status")
    public ResponseEntity <ChallengeAdminPostResponse> getUserPosts(
            @PathVariable UUID challengeId
//           , @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size
             )
    {
        String email = jwtTokenExtractor.getCurrentUserEmail();
        ChallengeAdminPostResponse response = challengeAdminService.getParticipatingUserPost(email,challengeId);
        return ResponseEntity.ok(response);
    }

}
