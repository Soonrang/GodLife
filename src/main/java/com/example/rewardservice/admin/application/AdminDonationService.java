package com.example.rewardservice.admin.application;


import com.example.rewardservice.admin.application.dto.request.DonationRegisterRequest;
import com.example.rewardservice.admin.application.dto.response.DonationResponse;
import com.example.rewardservice.donation.domain.Donation;
import com.example.rewardservice.donation.domain.DonationRepository;
import com.example.rewardservice.image.s3.S3ImageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdminDonationService {

    private final DonationRepository donationRepository;
    private final S3ImageService s3ImageService;

    public DonationResponse registerDonation(DonationRegisterRequest donationRegisterRequest) {

        //request에 image로, 서비스단에서는 String imageUrl로 바꿀것
        String image = s3ImageService.upload(donationRegisterRequest.getImageUrl());

        Donation donation = Donation.builder()
                .id(UUID.randomUUID())
                .title(donationRegisterRequest.getTitle())
                .currentAmount(donationRegisterRequest.getCurrentAmount())
                .targetAmount(donationRegisterRequest.getTargetAmount())
                .eventPeriod(donationRegisterRequest.getEventPeriod())  // EventPeriod 설정
                .donationImages(image)
                .build();

        validateDuplicateDonation(donation.getId());

        Donation savedDonation = donationRepository.save(donation);

        return new DonationResponse(
                savedDonation.getId(),
                savedDonation.getTitle(),
                savedDonation.getCurrentAmount(),
                savedDonation.getTargetAmount(),
                savedDonation.getEventPeriod(),
                savedDonation.getDonationImages()
        );
    }


    public void updateDonation(UUID id, DonationRegisterRequest donationRegisterRequest) {
        Donation donation = donationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("현재 찾으려는 기부아이디: " + id + " not found"));

        String originalImageUrl = donation.getDonationImages();
        String updateUrl = s3ImageService.upload(donationRegisterRequest.getImageUrl());

        donation.updateDonation(donationRegisterRequest.getTitle(),
                donationRegisterRequest.getCurrentAmount(),
                donationRegisterRequest.getTargetAmount(),
                updateUrl,
                donationRegisterRequest.getEventPeriod()
        );

        //저장된 이미지 삭제하는 로직
        if(originalImageUrl != updateUrl) {
            s3ImageService.deleteImageFromS3(originalImageUrl);
        }

        donationRepository.save(donation);
    }


    private void validateDuplicateDonation(UUID donationId) {
        if(donationRepository.findById(donationId).isPresent()) {
            throw new IllegalArgumentException("Donation with id " + donationId + " already exists");
        }
    }




}