package com.example.rewardservice.challenge.application.service;

import com.example.rewardservice.challenge.application.request.ChallengePostRequest;
import com.example.rewardservice.challenge.application.response.ChallengePostResponse;
import com.example.rewardservice.challenge.application.response.UserChallengeStateResponse;
import com.example.rewardservice.challenge.domain.Challenge;
import com.example.rewardservice.challenge.domain.ChallengePost;
import com.example.rewardservice.challenge.domain.UserChallenge;
import com.example.rewardservice.challenge.domain.repsoitory.ChallengePostRepository;
import com.example.rewardservice.challenge.domain.repsoitory.ChallengeRepository;
import com.example.rewardservice.challenge.domain.repsoitory.ChallengeUserRepository;
import com.example.rewardservice.image.s3.S3ImageService;
import com.example.rewardservice.user.domain.User;
import com.example.rewardservice.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChallengePostService {

    private final UserRepository userRepository;
    private final ChallengePostRepository challengePostRepository;
    private final ChallengeRepository challengeRepository;
    private final ChallengeUserRepository challengeUserRepository;
    private final S3ImageService s3ImageService;

    @Transactional
    public UUID createChallengePost(String email, UUID challengeId, ChallengePostRequest request) {
        User user = findByUserEmail(email);
        Challenge challenge = findByChallengeId(challengeId);
        UserChallenge userChallenge = findUserChallenge(email, challengeId);

        // 오늘 인증 기록이 있는지 확인
        LocalDate today = LocalDate.now();
        if (challengePostRepository.findTodayPostByUserAndChallenge(email, challengeId, today).isPresent()) {
            throw new IllegalStateException("오늘 이미 인증을 완료했습니다. 추가 인증이 불가합니다.");
        }

        String imageUrl = s3ImageService.upload(request.image());

        //이미지는 default : 인증
        ChallengePost challengePost = new ChallengePost(user, challenge, userChallenge, imageUrl, request.description());
        challengePostRepository.save(challengePost);

        //인증횟수 증가
        userChallenge.plusAuthCount();
        userChallenge.calculateProgress();

        challengeUserRepository.save(userChallenge);

        return challengePost.getId();
    }

    public Page<UserChallengeStateResponse> getUserChallengeStates(String email, UUID userChallengeId, int page, int size) {

        UserChallenge userChallenge = findByUserChallengeId(userChallengeId);

        Challenge challenge = userChallenge.getChallenge();
        Pageable pageable = PageRequest.of(page, size,Sort.by("createdAt").descending());

        Page<ChallengePost> challengePosts = challengePostRepository.findByUserChallengeId(userChallengeId, pageable);

        // ChallengePost를 UserChallengeStateResponse로 매핑
        return challengePosts.map(post -> UserChallengeStateResponse.from(challenge, userChallenge, List.of(post)));
    }

    //현재 로그인한 유저가 인증한 게시글만 반환
    public Page<ChallengePostResponse> getCurrentUserChallengePosts(UUID challengeId, String email, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ChallengePost> challengePosts = challengePostRepository.findAllByChallengeId(challengeId,email,pageable);
        return challengePosts.map(ChallengePostResponse::from);
    }



    public ChallengePostResponse viewPostDetail(UUID challengePostId) {
        ChallengePost challengePost = findChallengePost(challengePostId);

        return ChallengePostResponse.from(challengePost);
    }

    private User findByUserEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 이메일의 유저가 없습니다: " + email));
    }

    private Challenge findByChallengeId(UUID challengeId) {
        return challengeRepository.findById(challengeId)
                .orElseThrow(() -> new RuntimeException("해당 챌린지 아이디가 존재하지 않습니다." + challengeId));
    }

    private UserChallenge findByUserChallengeId(UUID userChallengeId){
        return challengeUserRepository.findById(userChallengeId)
                .orElseThrow(() -> new RuntimeException("참가중인 챌린지 아이디가 존재하지 않습니다." + userChallengeId));
    }

    private UserChallenge findUserChallenge(String email, UUID challengeId) {
        UserChallenge userChallenge = challengeUserRepository.findAllByChallengeIdAndUserEmail(challengeId, email);
        if (userChallenge == null) {
            throw new IllegalArgumentException("유저가 해당 챌린지에 참가중이지 않습니다.");
        }
        return userChallenge;
    }

    private ChallengePost findChallengePost(UUID challengePostId) {
        return challengePostRepository.findById(challengePostId)
                .orElseThrow(() -> new RuntimeException("해당 챌린지 인증 내역이 존재하지 않습니다."));
    }

}
