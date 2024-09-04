package com.example.rewardservice.challenge.application.service;

import com.example.rewardservice.challenge.application.request.ChallengeCreateRequest;
import com.example.rewardservice.challenge.application.request.ChallengeUpdateRequest;
import com.example.rewardservice.challenge.application.response.ChallengeInfoResponse;
import com.example.rewardservice.challenge.domain.Challenge;
import com.example.rewardservice.challenge.domain.ChallengeRepository;
import com.example.rewardservice.challenge.domain.vo.Images;
import com.example.rewardservice.common.ValidateService;
import com.example.rewardservice.image.s3.S3ImageService;
import com.example.rewardservice.user.domain.User;
import com.example.rewardservice.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final UserRepository userRepository;
    private final S3ImageService s3ImageService;

    public UUID createChallenge(String email, ChallengeCreateRequest challengeCreateRequest) {
        User user = findByUserEmail(email);

        String mainImageUrl = s3ImageService.upload(challengeCreateRequest.mainImage());
        String successImageUrl = s3ImageService.upload(challengeCreateRequest.successImage());
        String failImageUrl = s3ImageService.upload(challengeCreateRequest.failImage());

        Challenge challenge = challengeCreateRequest.toEntity(user,mainImageUrl,successImageUrl,failImageUrl);
        challengeRepository.save(challenge);
        return challenge.getId();
    }

    public Page<ChallengeInfoResponse> getChallenges(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Challenge> challenges = challengeRepository.findAll(pageable);

        return challenges.map(ChallengeInfoResponse::from);
    }



    //사진 제거 로직 필요
    public void updateChallenge(String email, UUID id, ChallengeUpdateRequest challengeUpdateRequest) {
        User user = findByUserEmail(email);
        Challenge challenge = findByChallengeId(id);

        if(!Objects.equals(challenge.getUser().getEmail(), user.getEmail())) {
            throw new IllegalArgumentException("챌린지를 등록한 유저가 아닙니다.");
        }

        Images updatedImages = updateImages(challenge.getImages(), challengeUpdateRequest);

        challengeUpdateRequest.updateEntity(challenge, updatedImages);
    }

    private Images updateImages(Images currentImages, ChallengeUpdateRequest request) {
        String mainImageUrl = currentImages.getMainImage();
        String successImageUrl = currentImages.getSuccessImage();
        String failImageUrl = currentImages.getFailImage();

        if (request.mainImage() != null && !request.mainImage().isEmpty()) {
            s3ImageService.deleteImageFromS3(mainImageUrl);
            mainImageUrl = s3ImageService.upload(request.mainImage());
        }
        if (request.successImage() != null && !request.successImage().isEmpty()) {
            s3ImageService.deleteImageFromS3(successImageUrl);
            successImageUrl = s3ImageService.upload(request.successImage());
        }
        if (request.failImage() != null && !request.failImage().isEmpty()) {
            s3ImageService.deleteImageFromS3(failImageUrl);
            failImageUrl = s3ImageService.upload(request.failImage());
        }

        return new Images(mainImageUrl, successImageUrl, failImageUrl);
    }


    public User findByUserEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 이메일의 유저가 없습니다: " + email));
    }

    public Challenge findByChallengeId(UUID challengeId) {
        return challengeRepository.findById(challengeId)
                .orElseThrow(() -> new RuntimeException("해당 id의 챌린지가 없습니다."));
    }


}
