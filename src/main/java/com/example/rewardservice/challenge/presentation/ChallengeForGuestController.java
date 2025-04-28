package com.example.rewardservice.challenge.presentation;

import com.example.rewardservice.challenge.application.response.ChallengeInfoResponse;
import com.example.rewardservice.challenge.application.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/guest/challenge")
@RequiredArgsConstructor
public class ChallengeForGuestController {

    private final ChallengeService challengeService;

    @GetMapping
    public ResponseEntity<Page<ChallengeInfoResponse>> getAllChallenges(@RequestParam(required = false) String category,
                                                                        @RequestParam(required = false) String state,
                                                                        @RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "12") int size) {
      Page<ChallengeInfoResponse> challenges = challengeService.getAllChallengesForGuest(page,size,category,state);
      return ResponseEntity.ok(challenges);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChallengeInfoResponse> getChallengeDetail(@PathVariable UUID id) {
        ChallengeInfoResponse challengeInfo = challengeService.viewDetail(null,id);

        return ResponseEntity.ok(challengeInfo);
    }

}
