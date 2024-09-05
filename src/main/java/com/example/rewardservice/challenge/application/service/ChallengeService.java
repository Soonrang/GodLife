package com.example.rewardservice.challenge.application.service;

import com.example.rewardservice.challenge.application.request.ChallengeCreateRequest;
import com.example.rewardservice.challenge.application.response.ChallengeInfoResponse;
import com.example.rewardservice.challenge.domain.Challenge;
import com.example.rewardservice.challenge.domain.ChallengeRepository;
import com.example.rewardservice.challenge.domain.vo.ChallengeImages;
import com.example.rewardservice.challenge.domain.vo.ChallengePeriod;
import com.example.rewardservice.common.BaseEntity;
import com.example.rewardservice.image.s3.S3ImageService;
import com.example.rewardservice.user.domain.User;
import com.example.rewardservice.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
                                .challengePeriod(challengePeriod)
                                .challengeImages(challengeImages)
                                .user(user)
                                .build();

        challenge.checkStatus(LocalDate.now());
        challengeRepository.save(challenge);

        return challenge.getId();
    }

    // 챌린지 목록 조회 로직
    public Page<ChallengeInfoResponse> getChallenges(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Challenge> challenges = challengeRepository.findAll(pageable);

        return challenges.map(ChallengeInfoResponse::from);
    }

    public Page<ChallengeInfoResponse> getChallengesByCategoryAndStatus(int page, int size, String category, String status) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Challenge> challenges = challengeRepository.findByCategoryAndState(category, status, pageable);

        return challenges.map(ChallengeInfoResponse::from);
    }

    public ChallengeInfoResponse viewDetail(UUID challengeId){
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new IllegalArgumentException("챌린지를 찾을 수 없습니다. ID: " + challengeId));

        challenge.checkStatus(LocalDate.now());
        challengeRepository.save(challenge);

        return ChallengeInfoResponse.from(challenge);
    }

    // 챌린지 수정 로직
//    @Transactional
//    public void updateChallenge(String email, UUID challengeId, ChallengeUpdateRequest request) {
//        User user = getUserByEmail(email);
//        Challenge challenge = getChallengeById(challengeId);
//
//        validateUser(challenge, user);
//
//        Images updatedImages = updateImages(challenge.getMainImage(), request);
//
//        request.updateEntity(challenge, updatedImages);
//        challengeRepository.save(challenge);  // 변경 사항을 저장
//    }

//    // 기존 이미지를 업데이트하고 필요 시 S3에서 삭제 후 업로드
//    private Images updateImages(Images currentImages, ChallengeUpdateRequest request) {
//        String mainImageUrl = currentImages.getMainImage();
//        String successImageUrl = currentImages.getSuccessImage();
//        String failImageUrl = currentImages.getFailImage();
//
//        if (isNotEmpty(request.getMainImage())) {
//            s3ImageService.deleteImageFromS3(mainImageUrl);
//            mainImageUrl = s3ImageService.upload(request.getMainImage());
//        }
//
//        if (isNotEmpty(request.getSuccessImage())) {
//            s3ImageService.deleteImageFromS3(successImageUrl);
//            successImageUrl = s3ImageService.upload(request.getSuccessImage());
//        }
//
//        if (isNotEmpty(request.getFailImage())) {
//            s3ImageService.deleteImageFromS3(failImageUrl);
//            failImageUrl = s3ImageService.upload(request.getFailImage());
//        }
//
//        return new Images(mainImageUrl, successImageUrl, failImageUrl);
//    }
//
//    // 이미지 업로드 메서드
//    private Images uploadImages(MultipartFile mainImage, MultipartFile successImage, MultipartFile failImage) {
//        String mainImageUrl = s3ImageService.upload(mainImage);
//        String successImageUrl = s3ImageService.upload(successImage);
//        String failImageUrl = s3ImageService.upload(failImage);
//
//        return new Images(mainImageUrl, successImageUrl, failImageUrl);
//    }

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


}