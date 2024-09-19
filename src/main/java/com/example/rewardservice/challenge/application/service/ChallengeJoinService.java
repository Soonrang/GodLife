package com.example.rewardservice.challenge.application.service;

import com.example.rewardservice.challenge.application.request.ChallengeJoinRequest;
import com.example.rewardservice.challenge.application.response.ChallengeInfoResponse;
import com.example.rewardservice.challenge.domain.Challenge;
import com.example.rewardservice.challenge.domain.ChallengeHistory;
import com.example.rewardservice.challenge.domain.UserChallenge;
import com.example.rewardservice.challenge.domain.repsoitory.ChallengeHistoryRepository;
import com.example.rewardservice.challenge.domain.repsoitory.ChallengeUserRepository;
import com.example.rewardservice.challenge.domain.repsoitory.ChallengeRepository;
import com.example.rewardservice.user.domain.User;
import com.example.rewardservice.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChallengeJoinService {

    private final UserRepository userRepository;
    private final ChallengeRepository challengeRepository;
    private final ChallengeUserRepository challengeUserRepository;
    private final ChallengeHistoryRepository challengeHistoryRepository;

    public static final String JOINED = "참가중";
    public static final String CANCELED = "참가취소";
    public static final String COMPLETED = "완료";

    @Transactional
    public UUID joinChallenge(ChallengeJoinRequest request, String email) {
        Challenge challenge = findByChallengeId(request.getChallengeId());
        User user = findByUserEmail(email);

        Optional<UserChallenge> latestParticipation = challengeUserRepository.findLatestByUserEmailAndChallengeId(email,request.getChallengeId());

        if(latestParticipation.isPresent()) {
            UserChallenge userChallenge = latestParticipation.get();
            String status = userChallenge.getStatus();

            if(status.equals(JOINED)) {
                throw new IllegalArgumentException("이미 참가중인 챌린지입니다.");
            } else if (status.equals(COMPLETED)) {
                throw new IllegalArgumentException("이미 완료된 챌린지입니다.");
            }
        }

        if(challenge.getParticipants() == challenge.getParticipantsLimit()) {
            throw new IllegalArgumentException("모집 인원수를 초과할 수 없습니다.");
        }

        // 참가 목록에 등록
        UserChallenge newParticipation = new UserChallenge(user, challenge, JOINED, true, request.getParticipationPoints());
        challengeUserRepository.save(newParticipation);

        // 참가 로그 기록
        ChallengeHistory challengeHistory = new ChallengeHistory(user,challenge,JOINED,"참가",request.getParticipationPoints());
        challengeHistoryRepository.save(challengeHistory);

        // 상금더하기
        challenge.plusPrize(request.getParticipationPoints());

        // 참가자 인원 증가
        challenge.participantsAmountPlus();

        // 유저 포인트 감소
        user.minusPoints(request.getParticipationPoints());

        if(challenge.getParticipantsLimit()-1 == challenge.getParticipants()) {
            challenge.changeState();
        }

        challengeRepository.save(challenge);
        userRepository.save(user);

        return newParticipation.getId();
    }

    @Transactional
    public UUID cancelChallenge(UUID challengeId, String email) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 챌린지가 없습니다: " + challengeId));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 이메일의 유저가 없습니다: " + email));

        UserChallenge currentChallenge = challengeUserRepository.findAllByChallengeIdAndUserEmail(challengeId, email);
        if (!currentChallenge.getStatus().equals(JOINED)) {
            throw new IllegalArgumentException("참가중인 챌린지가 아닙니다.");
        }

        if (LocalDate.now().isAfter(challenge.getChallengePeriod().getStartDate())) {
            throw new IllegalArgumentException("챌린지가 시작된 후에는 취소할 수 없습니다.");
        }

        // 챌린지 참가인원 마이너스
        challenge.participantsAmountMinus();

        // 유저 포인트 돌려줌
        user.earnPoints(currentChallenge.getDeposit());

        // 챌린지 전체 상금 마이너스
        challenge.minusPrize(currentChallenge.getDeposit());

        ChallengeHistory challengeHistory = new ChallengeHistory(user,challenge,CANCELED,"유저 요청으로 취소",currentChallenge.getDeposit());

        challenge.changeStateByCancel();

        challengeRepository.save(challenge);
        userRepository.save(user);

        challengeUserRepository.delete(currentChallenge);

        return challengeHistoryRepository.save(challengeHistory).getId();
    }


    public Page<ChallengeInfoResponse> getJoinedChallenges(String email, int page, int size, String status) {
        Pageable pageable = PageRequest.of(page,size);
        Page<UserChallenge> joinedChallenges = challengeUserRepository.findJoinedChallengesByUserEmail(email, status, pageable);

        return joinedChallenges.map(uc -> ChallengeInfoResponse.from2(uc.getChallenge(), uc.getUser(),true));
    }

    private User findByUserEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 이메일의 유저가 없습니다: " + email));
    }

    private Challenge findByChallengeId(UUID challengeId) {
        return challengeRepository.findById(challengeId)
                .orElseThrow(() -> new RuntimeException("해당 챌린지 아이디가 존재하지 않습니다." + challengeId));
    }

}
