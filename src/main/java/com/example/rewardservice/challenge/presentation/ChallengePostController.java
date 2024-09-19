package com.example.rewardservice.challenge.presentation;

import com.example.rewardservice.challenge.application.request.ChallengePostRequest;
import com.example.rewardservice.challenge.application.response.ChallengePostResponse;
import com.example.rewardservice.challenge.application.response.UserChallengeStateResponse;
import com.example.rewardservice.challenge.application.service.ChallengePostService;
import com.example.rewardservice.security.jwt.JwtTokenExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ChallengePostController {

    private final ChallengePostService challengePostService;
    private final JwtTokenExtractor jwtTokenExtractor;

    @PostMapping("/api/challenge/{challengeId}/posts")
    public ResponseEntity<UUID> createChallengePost(@PathVariable UUID challengeId, ChallengePostRequest request) {
        String email = jwtTokenExtractor.getCurrentUserEmail();
        UUID postId = challengePostService.createChallengePost(email, challengeId,request);
        return ResponseEntity.ok(postId);
    }

    @GetMapping("/api/challenge/{postId}/post-detail")
    public ResponseEntity<ChallengePostResponse> getChallengePostDetail(@PathVariable UUID postId) {
        ChallengePostResponse response = challengePostService.viewPostDetail(postId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/challenge/{userChallengeId}/details")
    public ResponseEntity<Page<UserChallengeStateResponse>> getChallengePostDetails(@PathVariable UUID userChallengeId,
                                                                              @RequestParam(defaultValue = "0") int page,
                                                                              @RequestParam(defaultValue = "10") int size
    ) {
        String email = jwtTokenExtractor.getCurrentUserEmail();

        Page<UserChallengeStateResponse> challengeInfo = challengePostService.getUserChallengeStates(email, userChallengeId, page, size);
        return ResponseEntity.ok(challengeInfo);
    }
}
