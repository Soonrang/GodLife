package com.example.rewardservice.challenge.application.service;

import com.example.rewardservice.challenge.application.response.ChallengeInfoResponse;
import com.example.rewardservice.challenge.domain.Challenge;
import com.example.rewardservice.challenge.domain.ChallengeHistory;
import com.example.rewardservice.challenge.domain.UserChallenge;
import com.example.rewardservice.challenge.domain.repsoitory.ChallengeHistoryRepository;
import com.example.rewardservice.challenge.domain.repsoitory.ChallengeJoinRepository;
import com.example.rewardservice.challenge.domain.repsoitory.ChallengeRepository;
import com.example.rewardservice.user.domain.User;
import com.example.rewardservice.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChallengeJoinService {

    private final UserRepository userRepository;
    private final ChallengeRepository challengeRepository;
    private final ChallengeJoinRepository challengeJoinRepository;
    private final ChallengeHistoryRepository challengeHistoryRepository;

    public static final String JOINED = "참가중";
    public static final String CANCELED = "참가취소";
    public static final String COMPLETED = "완료";

    public UUID joinChallenge(UUID challengeId, String email) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 챌린지가 없습니다: " + challengeId));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 이메일의 유저가 없습니다: " + email));

        Optional<UserChallenge> latestParticipation = challengeJoinRepository.findLatestByUserEmailAndChallengeId(email,challengeId);
        if(latestParticipation.isPresent()) {
            UserChallenge userChallenge = latestParticipation.get();
            String status = userChallenge.getStatus();

            if(status.equals(JOINED)) {
                throw new IllegalArgumentException("이미 참가중인 챌린지입니다.");
            } else if (status.equals(COMPLETED)) {
                throw new IllegalArgumentException("이미 완료된 챌린지입니다.");
            }
        }

        UserChallenge newParticipation = new UserChallenge(user, challenge, JOINED);
        ChallengeHistory challengeHistory = new ChallengeHistory(user,challengeId,JOINED,"참가");
        challengeJoinRepository.save(newParticipation);
        challengeHistoryRepository.save(challengeHistory);

        return newParticipation.getId();
    }

    public UUID cancelChallenge(UUID challengeId, String email) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 챌린지가 없습니다: " + challengeId));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 이메일의 유저가 없습니다: " + email));

        UserChallenge currentChallenge = challengeJoinRepository.findAllByChallengeIdAndUserEmail(challengeId, email);
        if (!currentChallenge.getStatus().equals(JOINED)) {
            throw new IllegalArgumentException("참가중인 챌린지가 아닙니다.");
        }

        currentChallenge.changeStatus(CANCELED);
        ChallengeHistory challengeHistory = new ChallengeHistory(user,challengeId,CANCELED,"사유");
        return challengeHistoryRepository.save(challengeHistory).getId();
    }


    public List<ChallengeInfoResponse> getJoinedChallenges(String email) {
        List<UserChallenge> joinedChallenges = challengeJoinRepository.findJoinedChallengesByUserEmail(email);

        return joinedChallenges.stream()
                .map(uc -> ChallengeInfoResponse.from2(uc.getChallenge(), uc.getUser()))
                .collect(Collectors.toList());
    }

}
