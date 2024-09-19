package com.example.rewardservice.challenge.application.service;

import com.example.rewardservice.challenge.application.response.ChallengeHistoryResponse;
import com.example.rewardservice.challenge.domain.ChallengeHistory;
import com.example.rewardservice.challenge.domain.repsoitory.ChallengeHistoryRepository;
import com.example.rewardservice.user.domain.User;
import com.example.rewardservice.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChallengeHistoryService {

    private final ChallengeHistoryRepository challengeHistoryRepository;
    private final UserRepository userRepository;

    public Page<ChallengeHistoryResponse> getChallengeHistory(String email, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ChallengeHistory> challengeHistories = challengeHistoryRepository.findChallengeHistoriesByUserEmail(email,pageable);

        return challengeHistories.map(challengeHistory ->
                new ChallengeHistoryResponse(
                        challengeHistory.getChallenge().getId(),
                        challengeHistory.getChallenge().getTitle(),
                        challengeHistory.getStatus(),
                        challengeHistory.getCreateAt(),
                        challengeHistory.getReason(),
                        challengeHistory.getPoint()
                )
        );
    }

    private User findByUserEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 이메일의 유저가 없습니다: " + email));
    }
}
