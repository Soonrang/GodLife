package com.example.rewardservice.challenge.application.service;

import com.example.rewardservice.challenge.application.request.ChallengeCreateRequest;
import com.example.rewardservice.challenge.application.request.ChallengeUpdateRequest;
import com.example.rewardservice.challenge.application.response.ChallengeInfoResponse;
import com.example.rewardservice.challenge.domain.Challenge;
import com.example.rewardservice.challenge.domain.ChallengeHistory;
import com.example.rewardservice.challenge.domain.UserChallenge;
import com.example.rewardservice.challenge.domain.repsoitory.ChallengeRepository;
import com.example.rewardservice.challenge.domain.vo.ChallengeImages;
import com.example.rewardservice.challenge.domain.vo.ChallengePeriod;
import com.example.rewardservice.common.BaseEntity;
import com.example.rewardservice.image.s3.S3ImageService;
import com.example.rewardservice.user.domain.User;
import com.example.rewardservice.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ChallengeService extends BaseEntity {

    private final ChallengeRepository challengeRepository;
    private final UserRepository userRepository;
    private final S3ImageService s3ImageService;

    public UUID createChallenge(String email, ChallengeCreateRequest request) {
        User user = findByUserEmail(email);

        ChallengePeriod challengePeriod = new ChallengePeriod(request.getStartDate(),
                request.getEndDate(), request.getUploadStartTime(), request.getUploadEndTime());

        String mainImageUrl = s3ImageService.upload(request.getMainImage());
        String successImageUrl = s3ImageService.upload(request.getSuccessImage());
        String failImageUrl = s3ImageService.upload(request.getFailImage());

        ChallengeImages challengeImages = new ChallengeImages(mainImageUrl, successImageUrl, failImageUrl);

        Challenge challenge = Challenge.builder()
                                .title(request.getTitle())
                                .category(request.getCategory())
                                .participantsLimit(request.getParticipantsLimit())
                                .description(request.getDescription())
                                .authMethod(request.getAuthMethod())
                                .state("최초 등록")
                                .participants(0)
                                .challengePeriod(challengePeriod)
                                .challengeImages(challengeImages)
                                .user(user)
                                .isDeleted(false)
                                .isClosed(false)
                                .build();

        challenge.checkStatus(LocalDate.now());
        challengeRepository.save(challenge);

        return challenge.getId();
    }

    // 챌린지 목록 조회 로직
    public Page<ChallengeInfoResponse> getChallenges(String email, int page, int size) {
        User user = findByUserEmail(email);
        Pageable pageable = PageRequest.of(page, size);
        Page<Challenge> challenges = challengeRepository.findChallenge(pageable);

        return challenges.map(challenge -> {
            boolean isJoined = checkIfUserJoinedChallenge(user, challenge);
            return ChallengeInfoResponse.from(challenge, isJoined);
        });
    }

    public Page<ChallengeInfoResponse> getChallengesByCategoryAndStatus(String email, int page, int size, String category, String status) {
        User user = findByUserEmail(email);
        Pageable pageable = PageRequest.of(page, size);
        Page<Challenge> challenges = challengeRepository.findByCategoryAndState(category, status, pageable);

        return challenges.map(challenge -> {
            boolean isJoined = checkIfUserJoinedChallenge(user, challenge);
            return ChallengeInfoResponse.from(challenge, isJoined);
        });
    }

    public ChallengeInfoResponse viewDetail(String email, UUID challengeId){
        User user = findByUserEmail(email);
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new IllegalArgumentException("챌린지를 찾을 수 없습니다. ID: " + challengeId));

        //challenge.checkStatus(LocalDate.now());
        challengeRepository.save(challenge);
        boolean isJoined = checkIfUserJoinedChallenge(user, challenge);

        return ChallengeInfoResponse.from(challenge, isJoined);
    }

     //챌린지 수정 로직 > admin서비스로 이동
    @Transactional
    public void updateChallenge(String email, UUID challengeId, ChallengeUpdateRequest request) {
        User user = findByUserEmail(email);
        Challenge challenge = getChallengeById(challengeId);

        validateUser(challenge, user);

        String mainImageUrl = challenge.getChallengeImages().getMainImage();
        String successImageUrl = challenge.getChallengeImages().getSuccessImage();
        String failImageUrl = challenge.getChallengeImages().getFailImage();

        if(request.getMainImage() != null) {
            mainImageUrl = s3ImageService.upload(request.getMainImage());
            challenge.getChallengeImages().changeMainImage(mainImageUrl);
        }

        if(request.getSuccessImage() != null) {
            successImageUrl = s3ImageService.upload(request.getSuccessImage());
            challenge.getChallengeImages().changeSuccessImage(successImageUrl);
        }

        if(request.getFailImage() != null) {
            failImageUrl = s3ImageService.upload(request.getFailImage());
            challenge.getChallengeImages().changeFailImage(failImageUrl);
        }

        ChallengePeriod challengePeriod = new ChallengePeriod(request.getStartDate(),request.getEndDate(),request.getUploadStartTime(),request.getUploadEndTime());
        ChallengeImages challengeImages = new ChallengeImages(mainImageUrl, successImageUrl, failImageUrl);

        challenge.updateChallenge(request.getTitle(), request.getCategory(), request.getParticipantsLimit()
        ,request.getDescription(),request.getAuthMethod(), challengePeriod,
                challengeImages);

        challengeRepository.save(challenge);  // 변경 사항을 저장
    }


    public void deleteChallenge(String email, UUID challengeId){
        User user = findByUserEmail(email);
        Challenge challenge = getChallengeById(challengeId);

        // 관리자 권한 확인
        if (isAdmin()) {
            // 관리자는 언제든지 삭제 가능
            refundPointsToParticipants(challenge);
            challenge.changeIsDelete(true);
            challengeRepository.save(challenge);
            return;
        }

        // 일반 유저는 챌린지가 시작되기 전에만 삭제 가능
        LocalDate today = LocalDate.now();
        if (challenge.getChallengePeriod().getStartDate().isBefore(today)) {
            throw new IllegalStateException("챌린지가 이미 시작되어 삭제할 수 없습니다.");
        }

        validateUser(challenge, user);
        refundPointsToParticipants(challenge);
        challenge.changeIsDelete(true);
        challengeRepository.save(challenge);
    }

    private void refundPointsToParticipants(Challenge challenge){
        List<UserChallenge> userChallenges = challenge.getUserChallenges();

        for (UserChallenge userChallenge : userChallenges) {
            User participant = userChallenge.getUser();
            long stakedPoints = userChallenge.getDeposit();

            participant.earnPoints(stakedPoints);

            userRepository.save(participant);

            ChallengeHistory challengeHistory = new ChallengeHistory(
                    participant,challenge,"삭제","사용자가 삭제한 챌린지입니다.",stakedPoints
            );
        }
    }

    private User findByUserEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 이메일의 유저가 없습니다: " + email));
    }
    // 챌린지 조회
    private Challenge getChallengeById(UUID challengeId) {
        return challengeRepository.findById(challengeId)
                .orElseThrow(() -> new IllegalArgumentException("챌린지를 찾을 수 없습니다. ID: " + challengeId));
    }

    // 사용자가 챌린지 생성자인지 검증
    private void validateUser(Challenge challenge, User user) {
        if (!Objects.equals(challenge.getUser().getEmail(), user.getEmail())) {
            throw new IllegalArgumentException("해당 챌린지를 수정할 권한이 없습니다.");
        }
    }

    private boolean checkIfUserJoinedChallenge(User user, Challenge challenge) {
        return challenge.getUserChallenges().stream()
                .anyMatch(userChallenge -> userChallenge.getUser().getId().equals(user.getId()));
    }

    public static boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getAuthorities() == null) {
            return false;
        }

        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals("ROLE_ADMIN"));
    }


}