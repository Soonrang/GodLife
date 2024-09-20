package com.example.rewardservice.challenge.application.service;

import com.example.rewardservice.challenge.application.response.ChallengeInfoResponse;
import com.example.rewardservice.challenge.application.response.ParticipantResponse;
import com.example.rewardservice.challenge.domain.ChallengePost;
import com.example.rewardservice.challenge.domain.UserChallenge;
import com.example.rewardservice.challenge.domain.repsoitory.ChallengeUserRepository;
import com.example.rewardservice.challenge.domain.repsoitory.ChallengePostRepository;
import com.example.rewardservice.challenge.domain.repsoitory.ChallengeRepository;
import com.example.rewardservice.user.domain.repository.UserRepository;
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
    private final UserRepository userRepository;


    public Page<ChallengeInfoResponse> getAdminChallenges(String email, int page, int size, String state) {
        Pageable pageable = PageRequest.of(page, size);


        // state 검증 코드 필요
        if (state == null) {
            return getAdminChallengesByAllStates(email, pageable);
        } else {
            return getJoinedChallengesBySpecificState(email, pageable, state);
        }
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


    private ChallengePost findByPostId(UUID id) {
        return challengePostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 id의 post가 없습니다: " + id));
    }

    public Page<ChallengeInfoResponse> getAdminChallengesByAllStates(String email, Pageable pageable) {
        Page<UserChallenge> joinedChallenges = challengeUserRepository.findAdminChallengesByAllStates(email, pageable);
        return joinedChallenges.map(uc -> {
            boolean hasCheckedInToday = hasCheckedInToday(email, uc.getChallenge().getId());
            return ChallengeInfoResponse.from3(uc.getChallenge(), uc.getUser(), true, hasCheckedInToday, uc.getId());
        });
    }

    public Page<ChallengeInfoResponse> getJoinedChallengesBySpecificState(String email, Pageable pageable, String state) {
        Page<UserChallenge> joinedChallenges = challengeUserRepository.findAdminChallengesByUserEmail(email, state, pageable);
        return joinedChallenges.map(uc -> {
            boolean hasCheckedInToday = hasCheckedInToday(email, uc.getChallenge().getId());
            return ChallengeInfoResponse.from3(uc.getChallenge(), uc.getUser(), true, hasCheckedInToday, uc.getId());
        });
    }

    // 오늘 인증 여부를 확인하는 메서드
    private boolean hasCheckedInToday(String email, UUID challengeId) {
        LocalDate today = LocalDate.now();
        return challengePostRepository.findTodayPostByUserAndChallenge(email, challengeId, today).isPresent();
    }


}
