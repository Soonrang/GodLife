package com.example.rewardservice.challenge.application.service;

import com.example.rewardservice.challenge.application.response.ChallengeAdminPostResponse;
import com.example.rewardservice.challenge.application.response.ChallengeInfoResponse;
import com.example.rewardservice.challenge.application.response.ParticipantResponse;
import com.example.rewardservice.challenge.domain.Challenge;
import com.example.rewardservice.challenge.domain.ChallengeHistory;
import com.example.rewardservice.challenge.domain.ChallengePost;
import com.example.rewardservice.challenge.domain.UserChallenge;
import com.example.rewardservice.challenge.domain.repsoitory.*;
import com.example.rewardservice.user.domain.User;
import com.example.rewardservice.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChallengeAdminService {

    private final ChallengeRepository challengeRepository;
    private final ChallengeUserRepository challengeUserRepository;
    private final ChallengePostRepository challengePostRepository;
    private final ChallengeAdminRepository challengeAdminRepository;
    private final ChallengeHistoryRepository challengeHistoryRepository;
    private final UserRepository userRepository;

    public static final long FAIL_CHALLENGE_POINTS = 0;
    public static final String FAIL_CHALLENGE_MESSAGE = "실패";
    public static final String SUCCESS_CHALLENGE_MESSAGE = "성공";
    public static final String ALL_USER_FAIL_CHALLENGE_MESSAGE = "회수";
    public static final String ALL_USER_FAIL_CHALLENGE_INFORM = "모든유저가 실패했습니다.";
    public static final long SUCCESS_CHALLENGE_CRITERIA = 85;

    @Transactional
    public void closeChallenge(String email, UUID challengeId) {
        // 유저 validate
        Challenge challenge = findByChallengeId(challengeId);
        validateIsClosed(challenge);

        List<UserChallenge> participants = challengeUserRepository.findByChallengeId(challengeId);

        long successCount = participants.stream()
                .filter(userChallenge -> userChallenge.getProgress() >= SUCCESS_CHALLENGE_CRITERIA)
                .count();

        if(participants.isEmpty() || successCount == 0) {
           handleNoSuccessParticipants(challenge);
        } else {
           handleSuccessParticipants(participants, challenge, successCount);
        }
        changeStateToClose(challenge);
    }

    private void failureUser(UserChallenge userChallenge, Challenge challenge){
        User user = userChallenge.getUser();
        ChallengeHistory failHistory = new ChallengeHistory(user,challenge,FAIL_CHALLENGE_MESSAGE," ", FAIL_CHALLENGE_POINTS);
        challengeHistoryRepository.save(failHistory);
    }

    private long calcPerPrize (Challenge challenge, long successCount) {
        long totalPrize = challenge.getPrize();

        return totalPrize/successCount;
    }

    private void handleNoSuccessParticipants(Challenge challenge) {
        User admin = findAdmin();
        admin.transToAdmin(challenge.getPrize());
        ChallengeHistory challengeHistory = new ChallengeHistory(admin,challenge,ALL_USER_FAIL_CHALLENGE_MESSAGE,ALL_USER_FAIL_CHALLENGE_INFORM, challenge.getPrize());
        challengeHistoryRepository.save(challengeHistory);
    }

    private void handleSuccessParticipants(List<UserChallenge> participants, Challenge challenge, long successCount) {
        long prizePerUser = calcPerPrize(challenge, successCount);

        for (UserChallenge userChallenge : participants) {
            if (userChallenge.getProgress() >= SUCCESS_CHALLENGE_CRITERIA) {
                successUser(userChallenge, challenge, prizePerUser);  // 성공한 사용자 처리
            } else {
                failureUser(userChallenge, challenge);  // 실패한 사용자 처리
            }
        }
    }

    private void changeStateToClose(Challenge challenge) {
        if(!challenge.getState().equals("종료")){
            challenge.changeStateClosed();
        }
        challenge.changeIsClosed();
        challengeRepository.save(challenge);
    }

    public Page<ChallengeInfoResponse> getAdminChallengesByUserEmail(String email, int page, int size, String state) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Challenge> challenges;

        if (state == null) {
            challenges = challengeAdminRepository.findAdminChallengesByAllStates(email, pageable);
        } else {
            challenges = challengeAdminRepository.findAdminChallengesByUserEmail(email, state, pageable);
        }

        return challenges.map(c -> ChallengeInfoResponse.from(c, true));
    }


    public List<ParticipantResponse> getParticipants(UUID challengeId) {
        List<UserChallenge> participants = challengeUserRepository.findByChallengeId(challengeId);

        return participants.stream()
                .map(userChallenge -> new ParticipantResponse(
                        userChallenge.getUser().getId(),
                        userChallenge.getId(),
                        userChallenge.getUser().getEmail(),
                        userChallenge.getUser().getName(),
                        userChallenge.getUser().getNickname(),
                        userChallenge.getProgress(),
                        userChallenge.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    public void updateChallengePostStatus(UUID challengeId, UUID postId, String status) {
        // 챌린지 업로드 유저인지 확인 , 기간 검증

        // 개설한 유저가 최종적으로 종료한 챌린지인 경우 예외처리
        Challenge challenge = findByChallengeId(challengeId);
        validateIsClosed(challenge);

        ChallengePost post = findByPostId(postId);

        if(Objects.equals(post.getStatus(), status)) {
            throw new IllegalArgumentException("이미" + status + "상태입니다.");
        }

        UserChallenge userChallenge = post.getUserChallenge();

        if(Objects.equals(status, "인증실패")) {
            userChallenge.minusAuthCount();
            userChallenge.calculateProgress();
            post.changeStatus(status);
        } else if (Objects.equals(status, "인증")) {
            userChallenge.plusAuthCount();
            userChallenge.calculateProgress();
            post.changeStatus(status);
        } else {
            throw new IllegalArgumentException("잘못된 status 값입니다. (인증/인증실패만 가능)" + status);
        }

        challengePostRepository.save(post);
    }

    public ChallengeAdminPostResponse getParticipatingUserPost(String email,LocalDate filterDate, UUID challengeId) {
        //Pageable pageable = PageRequest.of(page, size);

        // 관리자 권한이 있는지 확인

        Challenge challenge = findByChallengeId(challengeId);

        List<ChallengePost> posts = challengePostRepository.findByChallengeId(challengeId,filterDate);

        return ChallengeAdminPostResponse.from(challenge, posts);
    }

    private void successUser(UserChallenge userChallenge, Challenge challenge, long prizePerUser){
        User user = userChallenge.getUser();
        ChallengeHistory challengeHistory = new ChallengeHistory(user,challenge,SUCCESS_CHALLENGE_MESSAGE," ", prizePerUser);
        challengeHistoryRepository.save(challengeHistory);

        user.earnPoints(prizePerUser);
        userRepository.save(user);
    }



    private ChallengePost findByPostId(UUID id) {
        return challengePostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 id의 post가 없습니다: " + id));
    }

    private Challenge findByChallengeId(UUID id) {
        return challengeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 id의 챌린지가 존재하지 않습니다."));
    }

    private User findAdmin() {
        return userRepository.findByEmail("admin@gmail.com")
                .orElseThrow(() -> new RuntimeException("어드민 계정이 올바르지 않습니다."));
    }


    private void validateIsClosed(Challenge challenge){
        if(challenge.isClosed()) {
            throw new IllegalArgumentException("이미 종료된 챌린지입니다.");
        }
    }

}
