package com.example.rewardservice.challenge.presentation;

import com.example.rewardservice.challenge.application.request.ChallengeCreateRequest;
import com.example.rewardservice.challenge.application.request.ChallengeUpdateRequest;
import com.example.rewardservice.challenge.application.response.ChallengeInfoResponse;
import com.example.rewardservice.challenge.application.service.ChallengeService;
import com.example.rewardservice.security.jwt.JwtTokenExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/challenge")
@RequiredArgsConstructor
public class ChallengeController {

    private final JwtTokenExtractor jwtTokenExtractor;
    private final ChallengeService challengeService;

//    @PostMapping
//    public ResponseEntity<UUID> createChallenge(
//            @RequestParam("title") String title,
//            @RequestParam("category") String category,
//            @RequestParam("startDate") String startDate,
//            @RequestParam("endDate") String endDate,
//            @RequestParam("participantsLimit") String participantsLimit,
//            @RequestParam("description") String description,
//            @RequestParam("authMethod") String authMethod,
//            @RequestParam("mainImage") MultipartFile mainImage,
//            @RequestParam("successImage") MultipartFile successImage,
//            @RequestParam("failImage") MultipartFile failImage) {
//
//        // LocalDateTime으로 변환
//        LocalDateTime start = LocalDateTime.parse(startDate);
//        LocalDateTime end = LocalDateTime.parse(endDate);
//        long participants = Long.parseLong(participantsLimit);
//
//        // ChallengeCreateRequest 객체 생성
//        ChallengeCreateRequest request = new ChallengeCreateRequest(
//                title, category, start, end, participants,
//                description, authMethod, mainImage, successImage, failImage
//        );
//
//        // 이메일 추출
//        String email = jwtTokenExtractor.getCurrentUserEmail();
//
//        // 챌린지 생성 서비스 호출
//        UUID challengeId = challengeService.createChallenge(email, request);
//        return ResponseEntity.ok(challengeId);
//    }


    @PostMapping
    public ResponseEntity<UUID> createChallenge(@ModelAttribute ChallengeCreateRequest request) {
        String email = jwtTokenExtractor.getCurrentUserEmail();
        UUID challengeId = challengeService.createChallenge(email,request);
        return ResponseEntity.ok(challengeId);
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<Void> updateChallenge(@PathVariable UUID id, @ModelAttribute ChallengeUpdateRequest request) {
//        String email = jwtTokenExtractor.getCurrentUserEmail();
//        challengeService.updateChallenge(email, id,request);
//        return ResponseEntity.noContent().build();
//    }

    @GetMapping
    public ResponseEntity<Page<ChallengeInfoResponse>> getChallenges(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<ChallengeInfoResponse> challenges = challengeService.getChallenges(page, size);
        return ResponseEntity.ok(challenges);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<ChallengeInfoResponse>> getChallengesByCategoryAndStatus(
            @RequestParam String category,
            @RequestParam String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // 서비스 메서드를 호출해 카테고리와 상태로 챌린지를 조회
        Page<ChallengeInfoResponse> challenges = challengeService.getChallengesByCategoryAndStatus(page, size, category, status);
        return ResponseEntity.ok(challenges);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChallengeInfoResponse> getChallengeDetail(@PathVariable UUID id) {
        // 서비스 로직을 통해 챌린지 상세 조회
        ChallengeInfoResponse challengeInfo = challengeService.viewDetail(id);

        // 결과 반환
        return ResponseEntity.ok(challengeInfo);
    }

}
